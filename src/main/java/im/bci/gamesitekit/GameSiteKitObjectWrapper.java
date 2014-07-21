/*
The MIT License (MIT)

Copyright (c) 2014 devnewton <devnewton@bci.im>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
package im.bci.gamesitekit;

import com.fasterxml.jackson.databind.JsonNode;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateBooleanModel;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateSequenceModel;

/**
 *
 * @author devnewton
 */
public class GameSiteKitObjectWrapper extends DefaultObjectWrapper {

    @Override
    protected TemplateModel handleUnknownType(Object obj) throws TemplateModelException {
        if (obj instanceof JsonNode) {
            return handleJsonNode((JsonNode) obj);
        } else {
            return super.handleUnknownType(obj);
        }
    }

    private TemplateModel handleJsonNode(JsonNode jsonNode) throws TemplateModelException {
        if (null != jsonNode) {
            switch (jsonNode.getNodeType()) {
                case ARRAY:
                    return new JsonNodeTemplateSequenceModel(jsonNode);
                case BINARY:
                case BOOLEAN:
                    return jsonNode.asBoolean() ? TemplateBooleanModel.TRUE : TemplateBooleanModel.FALSE;
                case MISSING:
                case NULL:
                    return wrap(null);
                case OBJECT:
                    return new JsonNodeTemplateHashModel(jsonNode);
                case POJO:
                case STRING:
                    return new SimpleScalar(jsonNode.asText());
            }
        }
        return wrap(null);
    }

    private class JsonNodeTemplateSequenceModel implements TemplateSequenceModel {

        private final JsonNode jsonNode;

        public JsonNodeTemplateSequenceModel(JsonNode jsonNode) {
            this.jsonNode = jsonNode;
        }

        @Override
        public TemplateModel get(int index) throws TemplateModelException {
            return handleJsonNode(jsonNode.get(index));
        }

        @Override
        public int size() throws TemplateModelException {
            return jsonNode.size();
        }
    }

    private class JsonNodeTemplateHashModel implements TemplateHashModel {

        private final JsonNode jsonNode;

        public JsonNodeTemplateHashModel(JsonNode jsonNode) {
            this.jsonNode = jsonNode;
        }

        @Override
        public TemplateModel get(String key) throws TemplateModelException {
            return handleJsonNode(jsonNode.get(key));
        }

        @Override
        public boolean isEmpty() throws TemplateModelException {
            return !jsonNode.fieldNames().hasNext();
        }
    }

}
