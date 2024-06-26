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

package org.gradle.launcher.daemon.toolchain;

import org.gradle.api.JavaVersion;
import org.gradle.internal.jvm.Jvm;
import org.gradle.jvm.toolchain.JvmImplementation;
import org.gradle.jvm.toolchain.JvmVendorSpec;

public class DaemonJvmCriteria {
    private final JavaVersion javaVersion;
    private final JvmVendorSpec vendorSpec;
    private final JvmImplementation jvmImplementation;

    public DaemonJvmCriteria(JavaVersion javaVersion, JvmVendorSpec vendorSpec, JvmImplementation jvmImplementation) {
        this.javaVersion = javaVersion;
        this.vendorSpec = vendorSpec;
        this.jvmImplementation = jvmImplementation;
    }

    public JavaVersion getJavaVersion() {
        return javaVersion;
    }

    public JvmVendorSpec getVendorSpec() {
        return vendorSpec;
    }

    public JvmImplementation getJvmImplementation() {
        return jvmImplementation;
    }

    public boolean isCompatibleWith(Jvm other) {
        return isCompatibleWith(other.getJavaVersion());
    }

    @Override
    public String toString() {
        // TODO: Include vendor and implementation
        return String.format("JVM version '%s'", getJavaVersion());
    }

    public boolean isCompatibleWith(JavaVersion javaVersion) {
        // TODO: Implement comparisons for vendorSpec and jvmImplementation
        return javaVersion == getJavaVersion(); // && vendorSpec.matches() && jvmImplementation == other.jvmImplementation;
    }
}
