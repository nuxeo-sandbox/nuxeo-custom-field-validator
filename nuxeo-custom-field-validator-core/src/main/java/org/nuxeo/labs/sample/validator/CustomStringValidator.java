/*
 * (C) Copyright 2021 Nuxeo (http://nuxeo.com/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Michael Vachette
 */

package org.nuxeo.labs.sample.validator;

import org.nuxeo.ecm.core.schema.types.resolver.AbstractObjectResolver;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CustomStringValidator extends AbstractObjectResolver {


    @Override
    public void configure(Map<String, String> parameters) throws IllegalArgumentException, IllegalStateException {
        super.configure(parameters);
        this.parameters.put("max",parameters.get("max"));
    }

    @Override
    public List<Class<?>> getManagedClasses() {
        return List.of(String.class);
    }

    @Override
    public String getName() {
        return "customStringValidator";
    }

    @Override
    public Object fetch(Object o) {
        if (o instanceof String) {
            int maxByteLength = Integer.parseInt((String) getParameters().get("max"));
            if (((String) o).getBytes().length <= maxByteLength) {
                return o;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public <T> T fetch(Class<T> aClass, Object o) {
        Object value = fetch(o);
        return aClass.isInstance(value) ? (T)value : null;
    }

    @Override
    public Serializable getReference(Object o) {
        return o instanceof String ? (Serializable) o : null;
    }

    @Override
    public String getConstraintErrorMessage(Object o, Locale locale) {
        return Helper.getConstraintErrorMessage(this, "max", o, locale);
    }

}
