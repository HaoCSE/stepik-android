package org.stepic.droid.model

import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.stepic.droid.testUtils.assertThatObjectParcelable

@RunWith(RobolectricTestRunner::class)
class OnboardingTypeTest {

    @Test
    fun isOnboardingTypeParcelable() {
        val onboardingType = OnboardingType.FIRST
        onboardingType.assertThatObjectParcelable<OnboardingType>()
    }
}
