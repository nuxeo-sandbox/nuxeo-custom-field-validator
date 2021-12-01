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

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.validation.DocumentValidationException;
import org.nuxeo.ecm.core.schema.SchemaManager;
import org.nuxeo.ecm.core.schema.types.Field;
import org.nuxeo.ecm.core.schema.types.Schema;
import org.nuxeo.ecm.core.schema.types.resolver.ObjectResolver;
import org.nuxeo.ecm.core.test.CoreFeature;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import javax.inject.Inject;

@RunWith(FeaturesRunner.class)
@Features({CoreFeature.class})
@Deploy({
        "nuxeo-custom-field-validator-core",
        "nuxeo-custom-field-validator-core:test-schema-contrib.xml"
})
public class TestCustomStringValidator {

    @Inject
    CoreSession session;

    @Inject
    SchemaManager schemaManager;

    @Test
    public void testResolverIsDeployed() {
        Schema schema = schemaManager.getSchema("custom");
        Field field = schema.getField("my_string");
        ObjectResolver resolver = field.getType().getObjectResolver();
        Assert.assertNotNull(resolver);
        Assert.assertTrue(resolver instanceof CustomStringValidator);
    }

    @Test
    public void testConstraintOk() {
        DocumentModel doc = session.createDocumentModel(session.getRootDocument().getPathAsString(),"Custom","Custom");
        doc.setPropertyValue("custom:my_string","a");
        doc = session.createDocument(doc);
    }

    @Test(expected = DocumentValidationException.class)
    public void testConstraintNotOk() {
        DocumentModel doc = session.createDocumentModel(session.getRootDocument().getPathAsString(),"Custom","Custom");
        doc.setPropertyValue("custom:my_string","abc1234567890");
        doc = session.createDocument(doc);
    }
}
