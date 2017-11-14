package org.stepic.droid.storage.dao

import android.content.ContentValues
import android.database.Cursor
import org.stepic.droid.mappers.toDbUrl
import org.stepic.droid.mappers.toVideoUrls
import org.stepic.droid.model.*
import org.stepic.droid.storage.operations.CrudOperations
import org.stepic.droid.storage.structure.DbStructureCachedVideo
import org.stepic.droid.storage.structure.DbStructureEnrolledAndFeaturedCourses
import org.stepic.droid.storage.structure.DbStructureVideoUrl
import org.stepic.droid.util.*
import javax.inject.Inject

class CourseDaoImpl @Inject
constructor(
        crudOperations: CrudOperations,
        private val cachedVideoDao: IDao<CachedVideo>,
        private val externalVideoUrlIDao: IDao<DbVideoUrl>,
        private val tableName: String)
    : DaoBase<Course>(crudOperations) {

    public override fun parsePersistentObject(cursor: Cursor): Course {
        val course = Course()
        course.lastStepId = cursor.getString(DbStructureEnrolledAndFeaturedCourses.Column.LAST_STEP_ID)
        course.certificate = cursor.getString(DbStructureEnrolledAndFeaturedCourses.Column.CERTIFICATE)
        course.workload = cursor.getString(DbStructureEnrolledAndFeaturedCourses.Column.WORKLOAD)
        course.courseFormat = cursor.getString(DbStructureEnrolledAndFeaturedCourses.Column.COURSE_FORMAT)
        course.targetAudience = cursor.getString(DbStructureEnrolledAndFeaturedCourses.Column.TARGET_AUDIENCE)

        course.setId(cursor.getLong(DbStructureEnrolledAndFeaturedCourses.Column.COURSE_ID))
        course.summary = cursor.getString(DbStructureEnrolledAndFeaturedCourses.Column.SUMMARY)
        course.cover = cursor.getString(DbStructureEnrolledAndFeaturedCourses.Column.COVER_LINK)
        course.intro = cursor.getString(DbStructureEnrolledAndFeaturedCourses.Column.INTRO_LINK_VIMEO)
        course.title = cursor.getString(DbStructureEnrolledAndFeaturedCourses.Column.TITLE)
        course.language = cursor.getString(DbStructureEnrolledAndFeaturedCourses.Column.LANGUAGE)
        course.lastDeadline = cursor.getString(DbStructureEnrolledAndFeaturedCourses.Column.LAST_DEADLINE)
        course.description = cursor.getString(DbStructureEnrolledAndFeaturedCourses.Column.DESCRIPTION)
        course.instructors = DbParseHelper.parseStringToLongArray(cursor.getString(DbStructureEnrolledAndFeaturedCourses.Column.INSTRUCTORS))
        course.requirements = cursor.getString(DbStructureEnrolledAndFeaturedCourses.Column.REQUIREMENTS)
        course.enrollment = cursor.getInt(DbStructureEnrolledAndFeaturedCourses.Column.ENROLLMENT)
        course.sections = DbParseHelper.parseStringToLongArray(cursor.getString(DbStructureEnrolledAndFeaturedCourses.Column.SECTIONS))
        course.introVideoId = cursor.getLong(DbStructureEnrolledAndFeaturedCourses.Column.INTRO_VIDEO_ID)
        course.slug = cursor.getString(DbStructureEnrolledAndFeaturedCourses.Column.SLUG)
        course.scheduleLink = cursor.getString(DbStructureEnrolledAndFeaturedCourses.Column.SCHEDULE_LINK)
        course.scheduleLongLink = cursor.getString(DbStructureEnrolledAndFeaturedCourses.Column.SCHEDULE_LONG_LINK)
        course.beginDate = cursor.getString(DbStructureEnrolledAndFeaturedCourses.Column.BEGIN_DATE)
        course.endDate = cursor.getString(DbStructureEnrolledAndFeaturedCourses.Column.END_DATE)
        course.learnersCount = cursor.getLong(DbStructureEnrolledAndFeaturedCourses.Column.LEARNERS_COUNT)
        course.progress = cursor.getString(DbStructureEnrolledAndFeaturedCourses.Column.PROGRESS)
        course.rating = cursor.getDouble(DbStructureEnrolledAndFeaturedCourses.Column.AVERAGE_RATING)
        course.reviewSummary = cursor.getInt(DbStructureEnrolledAndFeaturedCourses.Column.REVIEW_SUMMARY)

        var isActive = true
        try {
            isActive = cursor.getInt(DbStructureEnrolledAndFeaturedCourses.Column.IS_ACTIVE) > 0
        } catch (exception: Exception) {
            //it can be null before migration --> default active
        }

        course.isActive = isActive

        return course
    }

    public override fun getContentValues(course: Course): ContentValues {
        val values = ContentValues()

        values.put(DbStructureEnrolledAndFeaturedCourses.Column.LAST_STEP_ID, course.lastStepId)
        values.put(DbStructureEnrolledAndFeaturedCourses.Column.COURSE_ID, course.courseId)
        values.put(DbStructureEnrolledAndFeaturedCourses.Column.SUMMARY, course.summary)
        values.put(DbStructureEnrolledAndFeaturedCourses.Column.COVER_LINK, course.cover)
        values.put(DbStructureEnrolledAndFeaturedCourses.Column.INTRO_LINK_VIMEO, course.intro)
        values.put(DbStructureEnrolledAndFeaturedCourses.Column.TITLE, course.title)
        values.put(DbStructureEnrolledAndFeaturedCourses.Column.LANGUAGE, course.language)
        values.put(DbStructureEnrolledAndFeaturedCourses.Column.LAST_DEADLINE, course.lastDeadline)
        values.put(DbStructureEnrolledAndFeaturedCourses.Column.DESCRIPTION, course.description)

        val instructorsParsed = DbParseHelper.parseLongArrayToString(course.instructors)
        values.put(DbStructureEnrolledAndFeaturedCourses.Column.INSTRUCTORS, instructorsParsed)

        values.put(DbStructureEnrolledAndFeaturedCourses.Column.REQUIREMENTS, course.requirements)
        values.put(DbStructureEnrolledAndFeaturedCourses.Column.ENROLLMENT, course.enrollment)

        val sectionsParsed = DbParseHelper.parseLongArrayToString(course.sections)
        values.put(DbStructureEnrolledAndFeaturedCourses.Column.SECTIONS, sectionsParsed)

        values.put(DbStructureEnrolledAndFeaturedCourses.Column.WORKLOAD, course.workload)
        values.put(DbStructureEnrolledAndFeaturedCourses.Column.COURSE_FORMAT, course.courseFormat)
        values.put(DbStructureEnrolledAndFeaturedCourses.Column.TARGET_AUDIENCE, course.targetAudience)
        values.put(DbStructureEnrolledAndFeaturedCourses.Column.CERTIFICATE, course.certificate)
        values.put(DbStructureEnrolledAndFeaturedCourses.Column.SLUG, course.slug)

        values.put(DbStructureEnrolledAndFeaturedCourses.Column.SCHEDULE_LINK, course.scheduleLink)
        values.put(DbStructureEnrolledAndFeaturedCourses.Column.SCHEDULE_LONG_LINK, course.scheduleLongLink)

        values.put(DbStructureEnrolledAndFeaturedCourses.Column.BEGIN_DATE, course.beginDate)
        values.put(DbStructureEnrolledAndFeaturedCourses.Column.END_DATE, course.endDate)
        values.put(DbStructureEnrolledAndFeaturedCourses.Column.IS_ACTIVE, course.isActive)
        values.put(DbStructureEnrolledAndFeaturedCourses.Column.LEARNERS_COUNT, course.learnersCount)
        values.put(DbStructureEnrolledAndFeaturedCourses.Column.PROGRESS, course.progress)
        values.put(DbStructureEnrolledAndFeaturedCourses.Column.AVERAGE_RATING, course.rating)
        values.put(DbStructureEnrolledAndFeaturedCourses.Column.REVIEW_SUMMARY, course.reviewSummary)

        val video = course.introVideo
        if (video != null) {
            values.put(DbStructureEnrolledAndFeaturedCourses.Column.INTRO_VIDEO_ID, video.id)
        }
        return values
    }

    public override fun getDbName() = tableName

    public override fun getDefaultPrimaryColumn() = DbStructureEnrolledAndFeaturedCourses.Column.COURSE_ID

    public override fun getDefaultPrimaryValue(persistentObject: Course) = persistentObject.courseId.toString()

    override fun get(whereColumnName: String, whereValue: String): Course? {
        val course = super.get(whereColumnName, whereValue)
        addInnerObjects(course)
        return course
    }

    override fun getAllWithQuery(query: String, whereArgs: Array<String>?): List<Course> {
        val courseList = super.getAllWithQuery(query, whereArgs)
        for (course in courseList) {
            addInnerObjects(course)
        }
        return courseList
    }

    private fun addInnerObjects(course: Course?) {
        if (course == null) return
        val video = cachedVideoDao.get(DbStructureCachedVideo.Column.VIDEO_ID, course.introVideoId.toString())
        if (video != null) {
            val dbVideoUrls = externalVideoUrlIDao
                    .getAll(DbStructureVideoUrl.Column.videoId, course.introVideoId.toString())
            val videoUrls = dbVideoUrls.toVideoUrls()
            course.introVideo = transformCachedVideoToRealVideo(video, videoUrls)
        }
    }

    override fun insertOrUpdate(persistentObject: Course?) {
        super.insertOrUpdate(persistentObject)
        if (persistentObject != null && persistentObject.introVideo != null) {
            val video = persistentObject.introVideo
            val cachedVideo = video.transformToCachedVideo() //it is cached, but not stored video.
            cachedVideoDao.insertOrUpdate(cachedVideo)

            //add all urls for video
            val videoUrlList = video.urls
            if (videoUrlList.isNotEmpty()) {
                externalVideoUrlIDao.remove(DbStructureVideoUrl.Column.videoId, video.id.toString())
                videoUrlList.forEach { videoUrl ->
                    externalVideoUrlIDao.insertOrUpdate(videoUrl.toDbUrl(video.id))
                }
            }
        }
    }

    //// FIXME: 17.02.16 refactor this hack
    private fun transformCachedVideoToRealVideo(cachedVideo: CachedVideo?, videoUrls: List<VideoUrl>?): Video? {
        var realVideo: Video? = null
        if (cachedVideo != null) {
            realVideo = Video()
            realVideo.id = cachedVideo.videoId
            realVideo.thumbnail = cachedVideo.thumbnail

            val resultUrls: List<VideoUrl>
            if (videoUrls != null && !videoUrls.isEmpty()) {
                resultUrls = videoUrls
            } else {
                val videoUrl = VideoUrl()
                videoUrl.url = cachedVideo.url
                videoUrl.quality = cachedVideo.quality
                resultUrls = listOf(videoUrl)
            }

            realVideo.urls = resultUrls
        }
        return realVideo
    }
}
