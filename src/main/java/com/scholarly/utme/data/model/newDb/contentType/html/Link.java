package com.scholarly.utme.data.model.newDb.contentType.html;

public class Link {
    private String text;
    private int position;

    public Link(String text, int position) {
        this.text = text;
        this.position = position;
    }

    public String getText() {
        return text;
    }

    public int getPosition() {
        return position;
    }

    private class Destination {
        private String activity;
        private Object argument;

        public Destination(String activity, Object argument) {
            this.activity = activity;
            this.argument = argument;
        }

        public String getActivity() {
            return activity;
        }

        public Object getArgument() {
            return argument;
        }
    }
}
