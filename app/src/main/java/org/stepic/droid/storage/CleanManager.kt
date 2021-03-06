package org.stepic.droid.storage

import org.stepic.droid.model.Lesson
import org.stepic.droid.model.Step

interface CleanManager {

    fun removeSection(sectionId: Long)

    fun removeLesson(lesson: Lesson?)

    fun removeStep(step: Step?)
}
