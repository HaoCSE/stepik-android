package org.stepic.droid.core.presenters

import org.stepic.droid.core.filters.contract.FiltersPoster
import org.stepic.droid.core.presenters.contracts.FiltersView
import org.stepic.droid.di.catalog.CatalogScope
import org.stepic.droid.model.StepikFilter
import org.stepic.droid.preferences.SharedPreferenceHelper
import java.util.*
import javax.inject.Inject

@CatalogScope
class FiltersPresenter
@Inject
constructor(
        private val sharedPreferenceHelper: SharedPreferenceHelper,
        private val filtersPoster: FiltersPoster
) : PresenterBase<FiltersView>() {

    fun onNeedFilters() {
        view?.onFiltersPrepared(sharedPreferenceHelper.filterForFeatured)
    }

    fun onFilterChanged(newAppliedFilters: EnumSet<StepikFilter>) {
        sharedPreferenceHelper.saveFilterForFeatured(newAppliedFilters)
        filtersPoster.filtersChanged(newAppliedFilters)
    }
}
