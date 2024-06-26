/*
 * Copyright 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.internal.logging.console;

import com.google.common.base.CharMatcher;
import org.apache.commons.lang.StringUtils;
import org.gradle.internal.Either;
import org.gradle.internal.logging.events.OutputEventListener;
import org.gradle.internal.logging.events.PromptOutputEvent;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicReference;

public class DefaultUserInputReceiver implements GlobalUserInputReceiver {
    private final AtomicReference<UserInputReceiver> delegate = new AtomicReference<UserInputReceiver>();
    private OutputEventListener console;

    /**
     * This instance requires access to the "console" pipeline in order to prompt the user when there are validation problems.
     * However, the console pipeline also needs access to this instance in order to handle user input prompt requests.
     * Break this cycle for now using a non-final field.
     */
    public void attachConsole(OutputEventListener console) {
        this.console = console;
    }

    @Override
    public void readAndForwardText(final PromptOutputEvent event) {
        UserInputReceiver userInput = delegate.get();
        if (userInput == null) {
            throw new IllegalStateException("User input has not been initialized.");
        }
        userInput.readAndForwardText(new UserInputReceiver.Normalizer() {
            @Nullable
            @Override
            public String normalize(String text) {
                Either<?, String> result = event.convert(CharMatcher.javaIsoControl().removeFrom(StringUtils.trim(text)));
                if (result.getRight().isPresent()) {
                    // Need to prompt the user again
                    console.onOutput(new PromptOutputEvent(event.getTimestamp(), result.getRight().get(), false));
                    return null;
                } else {
                    // Send result
                    return result.getLeft().get().toString();
                }
            }
        });
    }

    @Override
    public void dispatchTo(UserInputReceiver userInput) {
        if (!delegate.compareAndSet(null, userInput)) {
            throw new IllegalStateException("User input has already been initialized.");
        }
    }

    @Override
    public void stopDispatching() {
        delegate.set(null);
    }
}