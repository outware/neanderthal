package au.com.outware.neanderthal.presentation.view.activity

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import au.com.outware.neanderthal.R
import au.com.outware.neanderthal.application.NeanderthalApplication
import au.com.outware.neanderthal.dagger.component.DaggerVariantListComponent
import au.com.outware.neanderthal.dagger.module.VariantListModule
import au.com.outware.neanderthal.presentation.adapter.VariantAdapter
import au.com.outware.neanderthal.presentation.presenter.EditVariantPresenter
import au.com.outware.neanderthal.presentation.presenter.VariantListPresenter
import au.com.outware.neanderthal.util.DividerItemDecoration
import au.com.outware.neanderthal.util.extensions.*
import kotlinx.android.synthetic.main.neanderthal_activity_variant_list.*
import javax.inject.Inject

class VariantListActivity : AppCompatActivity(), VariantListPresenter.ViewSurface {
    @Inject
    lateinit internal var presenter: VariantListPresenter

    private lateinit var adapter: VariantAdapter
    private var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.neanderthal_activity_variant_list)

        adapter = VariantAdapter { name, position -> presenter.onItemSelected(name, position) }

        DaggerVariantListComponent.builder()
                .neanderthalApplicationComponent(NeanderthalApplication.neanderthalApplicationComponent)
                .variantListModule(VariantListModule(this, adapter))
                .build()
                .inject(this)

        presenter.onCreate(savedInstanceState)

        variantList.layoutManager = LinearLayoutManager(this)
        variantList.addItemDecoration(DividerItemDecoration(this, android.R.drawable.divider_horizontal_bright))
        variantList.adapter = adapter

        buttonAdd.setOnClickListener { view -> presenter.onAddClicked() }
        buttonLaunch.setOnClickListener { view -> presenter.onLaunchClicked() }
    }

    override fun onCreateOptionsMenu(menu: Menu?) = inflateMenu(R.menu.neanderthal_menu_variant_list, menu)

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_item_edit -> presenter.onEditClicked()
            R.id.menu_item_delete -> presenter.onDeleteClicked()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        presenter.onSaveInstanceState(outState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == EditVariantPresenter.ViewSurface.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            presenter.onAddVariant(data!!.getStringExtra(EditVariantPresenter.ViewSurface.EXTRA_NAME)!!)
        }
    }

    override fun goToAddVariant() {
        navigateForResult(EditVariantActivity::class, EditVariantPresenter.ViewSurface.REQUEST_CODE)
    }

    override fun goToEditVariant(name: String) {
        navigate(EditVariantActivity::class, EditVariantPresenter.ViewSurface.EXTRA_NAME to name)
    }

    override fun goToMainApplication() {
        val resolvedInfos = packageManager.queryIntentActivities(Intent(Intent.ACTION_MAIN), PackageManager.GET_RESOLVED_FILTER)
        for(info in resolvedInfos) {
            val activityInfo = info.activityInfo
            val applicationName = activityInfo.applicationInfo.className

            if(applicationName != null && applicationName.equals(application.javaClass.name) &&
                    activityInfo.name != localClassName) {
                val intent = Intent();
                intent.component = ComponentName(this, activityInfo.name);
                startActivity(intent);
            }
        }
    }

    override fun createDeleteConfirmation() {
        dialog = AlertDialog.Builder(this)
                .setTitle(R.string.neanderthal_delete_title)
                .setMessage(R.string.neanderthal_delete_message)
                .setPositiveButton(R.string.neanderthal_delete_positive) { dialog, which -> presenter.onDeleteConfirmation(true) }
                .setNegativeButton(R.string.neanderthal_cancel) { dialog, which -> presenter.onDeleteConfirmation(false) }
                .show()
    }

    override fun dismissDeleteConfirmation() {
        dialog?.dismiss()
        this.dialog = null
    }

    override fun notifyDeleted() {
        layoutRoot.snackbar(R.string.neanderthal_delete_notice_message) {
            action(R.string.neanderthal_delete_notice_action) {
                presenter.onUndoClicked()
            }
        }
    }
}
