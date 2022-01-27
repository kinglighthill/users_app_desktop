package com.scholarly.utme.data.dao.newDb;

import com.scholarly.utme.data.model.newDb.ContentViewType;

import java.util.List;

public class SectionDao {

    private static final String idColumn = "_id";
    private static final String contentColumn = "content";
    private static final String parentSectionIdColumn = "parent_section_id";
    private static final String mainSectionOrderColumn = "main_section_order";
    private static final String childSectionOrderColumn = "child_section_order";
    private static final String contentViewTypeColumn = "content_view_type_id";



    public List<ContentViewType> getSections(int topicId, int sectionId) {
        int id = topicId;
        int idd = sectionId;
        return null;
    }

}
