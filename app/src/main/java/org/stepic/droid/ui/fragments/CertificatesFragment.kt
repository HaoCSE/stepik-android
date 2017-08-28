package org.stepic.droid.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.empty_certificates.*
import kotlinx.android.synthetic.main.fragment_certificates.*
import kotlinx.android.synthetic.main.need_auth_placeholder.*
import kotlinx.android.synthetic.main.progress_bar_on_empty_screen.*
import kotlinx.android.synthetic.main.report_problem_layout.*
import org.stepic.droid.R
import org.stepic.droid.base.App
import org.stepic.droid.base.FragmentBase
import org.stepic.droid.core.presenters.CertificatePresenter
import org.stepic.droid.core.presenters.contracts.CertificateView
import org.stepic.droid.model.CertificateViewItem
import org.stepic.droid.ui.activities.contracts.BottomNavigationViewRoot
import org.stepic.droid.ui.adapters.CertificatesAdapter
import org.stepic.droid.ui.dialogs.CertificateShareDialogFragment
import org.stepic.droid.ui.util.initCenteredToolbar
import org.stepic.droid.util.ProgressHelper
import javax.inject.Inject

class CertificatesFragment : FragmentBase(),
        CertificateView,
        SwipeRefreshLayout.OnRefreshListener {

    private var adapter: CertificatesAdapter? = null

    @Inject
    lateinit var certificatePresenter: CertificatePresenter

    override fun injectComponent() {
        App
                .component()
                .certificateComponentBuilder()
                .build()
                .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater?.inflate(R.layout.fragment_certificates, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        nullifyActivityBackground()
        super.onViewCreated(view, savedInstanceState)
        applyBottomMarginForRootView()

        initCenteredToolbar(R.string.certificates_title, false)

        adapter = CertificatesAdapter(certificatePresenter, activity)
        certificateRecyclerView.layoutManager = LinearLayoutManager(context)
        certificateRecyclerView.adapter = adapter

        authAction.setOnClickListener { screenManager.showLaunchScreen(activity) }

        certificateSwipeRefresh.setOnRefreshListener(this)
        certificateSwipeRefresh.setColorSchemeResources(
                R.color.stepic_brand_primary,
                R.color.stepic_orange_carrot,
                R.color.stepic_blue_ribbon)

        certificatePresenter.attachView(this)

        loadAndShowCertificates()
    }

    override fun onDestroyView() {
        certificatePresenter.detachView(this)
        authAction.setOnClickListener(null)
        super.onDestroyView()
    }

    private fun loadAndShowCertificates() {
        certificatePresenter.showCertificates(false)
    }

    override fun onLoading() {
        if (certificatePresenter.size() <= 0) {
            reportProblem.visibility = View.GONE
            reportEmptyCertificates.visibility = View.GONE
            ProgressHelper.activate(loadProgressbarOnEmptyScreen)
        }
    }

    override fun showEmptyState() {
        needAuthView.visibility = View.GONE
        ProgressHelper.dismiss(certificateSwipeRefresh)
        ProgressHelper.dismiss(loadProgressbarOnEmptyScreen)
        reportProblem.visibility = View.GONE
        if (certificatePresenter.size() <= 0) {
            reportEmptyCertificates.visibility = View.VISIBLE
        }
    }

    override fun onInternetProblem() {
        ProgressHelper.dismiss(certificateSwipeRefresh)
        ProgressHelper.dismiss(loadProgressbarOnEmptyScreen)
        reportEmptyCertificates.visibility = View.GONE
        needAuthView.visibility = View.GONE
        if (certificatePresenter.size() <= 0) {
            reportProblem.visibility = View.VISIBLE
        } else {
            Toast.makeText(context, R.string.connectionProblems, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDataLoaded(certificateViewItems: List<CertificateViewItem>) {
        ProgressHelper.dismiss(loadProgressbarOnEmptyScreen)
        ProgressHelper.dismiss(certificateSwipeRefresh)
        reportEmptyCertificates.visibility = View.GONE
        reportProblem.visibility = View.GONE
        needAuthView.visibility = View.GONE
        certificateSwipeRefresh.visibility = View.VISIBLE
        certificateRecyclerView.visibility = View.VISIBLE
        adapter?.updateCertificates(certificateViewItems)
    }

    override fun onNeedShowShareDialog(certificateViewItem: CertificateViewItem?) {
        if (certificateViewItem == null) {
            return
        }
        val bottomSheetDialogFragment = CertificateShareDialogFragment.newInstance(certificateViewItem)
        if (!bottomSheetDialogFragment.isAdded) {
            bottomSheetDialogFragment.show(fragmentManager, null)
        }
    }

    override fun onAnonymousUser() {
        ProgressHelper.dismiss(certificateSwipeRefresh)
        ProgressHelper.dismiss(loadProgressbarOnEmptyScreen)
        reportEmptyCertificates.visibility = View.GONE
        reportProblem.visibility = View.GONE
        certificateRecyclerView.visibility = View.GONE
        certificateSwipeRefresh.visibility = View.GONE
        needAuthView.visibility = View.VISIBLE
    }

    override fun onRefresh() {
        certificatePresenter.showCertificates(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean =
            when (item?.itemId) {
                android.R.id.home -> {
                    activity.finish()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }

    companion object {
        fun newInstance(): Fragment = CertificatesFragment()
    }


    override fun onResume() {
        super.onResume()
        (activity as? BottomNavigationViewRoot)?.disableAnyBehaviour()
    }

    override fun onPause() {
        super.onPause()
        (activity as? BottomNavigationViewRoot)?.resetDefaultBehaviour()
    }
}
