package org.stepic.droid.store.structure;

public final class DBStructureCourses extends DBStructureBase {

    private static String[] mUsedColumns = null;

    public static final String NAME = "courses";

    public static final class Column {
        public static final String ID = "_id";
        public static final String COURSE_ID = "course_id";
        public static final String SUMMARY = "summary";
        public static final String WORKLOAD = "workload";
        public static final String COVER_LINK = "cover";
        public static final String INTRO_LINK_VIMEO = "intro";
        public static final String COURSE_FORMAT = "course_format";
        public static final String TARGET_AUDIENCE = "target_audience";
        public static final String INSTRUCTORS = "instructors";
        public static final String REQUIREMENTS = "requirements";
        public static final String DESCRIPTION = "description";
        public static final String SECTIONS = "sections"; //'модули' in Russian
        public static final String TOTAL_UNITS = "total_units"; //'уроки' in Russian
        public static final String ENROLLMENT = "enrollment"; //'количество зарегистрированных' in Russian
        public static final String IS_FEATURED = "is_featured";
        public static final String OWNER = "owner";
        public static final String IS_CONTEST = "is_contest";
        public static final String LANGUAGE = "language";
        public static final String IS_PUBLIC = "is_public";
        public static final String TITLE = "title";
        public static final String SLUG = "slug";
        public static final String BEGIN_DATE_SOURCE = "begin_date_source";
        public static final String LAST_DEADLINE = "last_deadline";
    }

    public static String[] getUsedColumns() {
        if (mUsedColumns == null) {
            mUsedColumns = new String[]{
                    Column.ID,
                    Column.COURSE_ID,
                    Column.SUMMARY,
                    Column.COVER_LINK,
                    Column.INTRO_LINK_VIMEO,
                    Column.TITLE,
                    Column.LANGUAGE
            };
        }
        return mUsedColumns;
    }
}
