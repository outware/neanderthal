package au.com.outware.neanderthal.presentation.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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
import au.com.outware.neanderthal.util.extensions.action
import au.com.outware.neanderthal.util.extensions.inflateMenu
import au.com.outware.neanderthal.util.extensions.snackbar
import kotlinx.android.synthetic.main.neanderthal_activity_variant_list.*
import org.jetbrains.anko.AlertDialogBuilder
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import javax.inject.Inject

class VariantListActivity : AppCompatActivity(), VariantListPresenter.ViewSurface {
    @Inject
    lateinit internal var presenter: VariantListPresenter

    private lateinit var adapter: VariantAdapter
    private var dialog: AlertDialogBuilder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.neanderthal_activity_variant_list)

        adapter = VariantAdapter { name, position -> presenter.onItemSelected(name, position) }

        DaggerVariantListComponent.builder()
                .applicationComponent(NeanderthalApplication.applicationComponent)
                .variantListModule(VariantListModule(this, adapter))
                .build()
                .inject(this)

        presenter.onCreate(savedInstanceState)

        variantList.layoutManager = LinearLayoutManager(this)
        variantList.addItemDecoration(DividerItemDecoration(this, android.R.drawable.divider_horizontal_bright))
        variantList.adapter = adapter

        buttonAdd.setOnClickListener { view -> presenter.onAddClicked() }
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
        startActivityForResult<EditVariantActivity>(EditVariantPresenter.ViewSurface.REQUEST_CODE)
    }

    override fun goToEditVariant(name: String) {
        startActivity<EditVariantActivity>(EditVariantPresenter.ViewSurface.EXTRA_NAME to name)
    }

    override fun createDeleteConfirmation() {
        dialog = alert(R.string.neanderthal_delete_message, R.string.neanderthal_delete_title) {
            positiveButton(R.string.neanderthal_delete_positive) { presenter.onDeleteConfirmation(true) }
            negativeButton(R.string.neanderthal_cancel) { presenter.onDeleteConfirmation(false) }
        }.show()
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
