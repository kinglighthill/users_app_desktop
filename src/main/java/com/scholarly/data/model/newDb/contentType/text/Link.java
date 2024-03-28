package com.scholarly.data.model.newDb.contentType.text;

public class Link {
    private String text;
    private int position;
    private Destination destination;

    public Link(String text, int position, Destination destination) {
        this.text = text;
        this.position = position;
        this.destination = destination;
    }

    public String getText() {
        return text;
    }

    public int getPosition() {
        return position;
    }

    public Destination getDestination() {
        return destination;
    }

    private class Destination {
        private int subjectId;
        private int sectionId;

        public Destination(int subjectId, int sectionId) {
            this.subjectId = subjectId;
            this.sectionId = sectionId;
        }

        public int getSubjectId() {
            return subjectId;
        }

        public int getSectionId() {
            return sectionId;
        }
    }
}
