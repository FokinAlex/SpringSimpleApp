package ssa.models.wrappers;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

public class DataWrapper implements Wrapper {

    private Object data;

    @JsonIgnore
    private ContentBuilder contentBuilder = new ContentBuilder();

    public Object getData() {
        return this.data;
    }

    public ContentBuilder getContentBuilder() {
        return this.contentBuilder;
    }

    public class ContentBuilder {

        private Map<String, Object> content;

        private ContentBuilder() {
            this.content = new HashMap<>();
        }

        public ContentBuilder put(String name, Object value) {
            this.content.put(name, value);
            return this;
        }

        public void put() {
            if (null == DataWrapper.this.data) {
                DataWrapper.this.data = this.content;
            } else if (DataWrapper.this.data instanceof List) {
                ((List) DataWrapper.this.data).add(this.content);
            } else  {
                Object tempObject = DataWrapper.this.data;
                DataWrapper.this.data = new LinkedList() {{
                    this.add(tempObject);
                    this.add(content);
                }};
            }
            this.content = new HashMap<>();
        }
    }
}
