package au.com.outware.neanderthal.presentation.view.activity

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import au.com.outware.neanderthal.R
import au.com.outware.neanderthal.presentation.adapter.PropertyAdapter
import au.com.outware.neanderthal.presentation.presenter.EditVariantPresenter
import au.com.outware.neanderthal.util.extensions.finishWithResult
import au.com.outware.neanderthal.util.extensions.inflateMenu
import kotlinx.android.synthetic.main.neanderthal_activity_edit_variant.*
import kotlinx.android.synthetic.main.neanderthal_item_variant_name.view.*

/**
 * @author timmutton
 */
class EditVariantActivity : AppCompatActivity(), EditVariantPresenter.ViewSurface {
    lateinit private var presenter: EditVariantPresenter

    private var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.neanderthal_activity_edit_variant)

        val adapter = PropertyAdapter()

        presenter = EditVariantPresenter(this, adapter)

        val args = Bundle()
        intent?.extras?.let {
            args.putAll(intent.extras)
        }
        savedInstanceState?.let {
            args.putAll(it)
        }
        presenter.onCreate(args)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        propertyList.layoutManager = LinearLayoutManager(this)
        propertyList.adapter = adapter

        if(intent.hasExtra(EditVariantPresenter.ViewSurface.EXTRA_NAME)) {
            title = intent.getStringExtra(EditVariantPresenter.ViewSurface.EXTRA_NAME)
        } else {
            setTitle(R.string.neanderthal_title_add_variant)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?) = inflateMenu(R.menu.neanderthal_menu_edit_variant, menu)

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> presenter.onBackClicked()
            R.id.menu_item_done -> presenter.onDone()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        presenter.onBackClicked()
    }

    override fun goToVariantList(changesSaved: Boolean, name: String) {
        if(changesSaved) {
            finishWithResult(AppCompatActivity.RESULT_OK, EditVariantPresenter.ViewSurface.EXTRA_NAME to name)
        } else {
            finishWithResult(RESULT_CANCELED)
        }
    }

    override fun showNameError() {
        val nameValueLayout = propertyList.getChildAt(0).nameValueLayout
        nameValueLayout.isErrorEnabled = true
        nameValueLayout.error = getString(R.string.neanderthal_name_required)
    }

    override fun createCancelConfirmation() {
        dialog = AlertDialog.Builder(this)
                .setTitle(R.string.neanderthal_cancel_title)
                .setMessage(R.string.neanderthal_cancel_message)
                .setPositiveButton(R.string.neanderthal_cancel_positive) { dialog, which -> presenter.onCancelConfirmation(true) }
                .setNegativeButton(R.string.neanderthal_cancel) { dialog, which -> presenter.onCancelConfirmation(false) }
                .show()
    }

    override fun dismissCancelConfirmation() {
        dialog?.dismiss()
    }
}