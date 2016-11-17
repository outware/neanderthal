package au.com.outware.neanderthal.presentation.view.activity

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import au.com.outware.neanderthal.R
import au.com.outware.neanderthal.presentation.adapter.VariantAdapter
import au.com.outware.neanderthal.presentation.presenter.EditVariantPresenter
import au.com.outware.neanderthal.presentation.presenter.VariantListPresenter
import au.com.outware.neanderthal.util.DividerItemDecoration
import au.com.outware.neanderthal.util.extensions.*
import kotlinx.android.synthetic.main.neanderthal_activity_variant_list.*
import java.lang.System.exit

class VariantListActivity : AppCompatActivity(), VariantListPresenter.ViewSurface {
    lateinit private var presenter: VariantListPresenter

    private lateinit var adapter: VariantAdapter
    private var dialog: AlertDialog? = null
    private var allowEditing: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.neanderthal_activity_variant_list)

        adapter = VariantAdapter { name, position -> presenter.onItemSelected(name, position) }

        presenter = VariantListPresenter(this, adapter)
        presenter.onCreate(null)

        variantList.layoutManager = LinearLayoutManager(this)
        variantList.addItemDecoration(DividerItemDecoration(this, android.R.drawable.divider_horizontal_bright))
        variantList.adapter = adapter

        buttonAdd.setOnClickListener { view -> presenter.onAddClicked() }
    }

    override fun onCreateOptionsMenu(menu: Menu?) = inflateMenu(R.menu.neanderthal_menu_variant_list, menu)

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.neanderthal_menu_item_edit -> presenter.onEditClicked()
            R.id.neanderthal_menu_item_delete -> presenter.onDeleteClicked()
            R.id.neanderthal_menu_item_launch_application -> presenter.onLaunchClicked()
            R.id.neanderthal_menu_item_reset -> presenter.onResetToDefaultClicked()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.let {
            menu.findItem(R.id.neanderthal_menu_item_delete).setVisible(allowEditing)
            menu.findItem(R.id.neanderthal_menu_item_edit).setVisible(allowEditing)
        }

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
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
        var launchIntent = packageManager.getLaunchIntentForPackage(packageName)

        // If neanderthal is the default launch intent
        if(launchIntent.component.className.equals(localClassName)) {
            // Filter for main intents for this package
            val filterIntent = Intent(Intent.ACTION_MAIN)
            filterIntent.setPackage(packageName)

            // Get the first that isnt the current class
            val resolveInfo = packageManager.queryIntentActivities(filterIntent, PackageManager.GET_RESOLVED_FILTER)
                    .filter { info -> !info.activityInfo.name.equals(localClassName) }
                    .first()

            // Launch the activity
            launchIntent = Intent()
            launchIntent.component = ComponentName(this, resolveInfo.activityInfo.name)
        }

        val pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        manager.set(AlarmManager.RTC, System.currentTimeMillis(), pendingIntent)
        exit(0)
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

    override fun createResetConfirmation() {
        dialog = AlertDialog.Builder(this)
                .setTitle(R.string.neanderthal_reset_title)
                .setMessage(R.string.neanderthal_reset_message)
                .setPositiveButton(R.string.neanderthal_reset_positive) { dialog, which -> presenter.onResetConfirmation(true) }
                .setNegativeButton(R.string.neanderthal_cancel) { dialog, which -> presenter.onResetConfirmation(false) }
                .show()
    }

    override fun dismissResetConfirmation() {
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

    override fun notifyReset() {
        layoutRoot.snackbar(R.string.neanderthal_reset_notice_message)
    }

    override fun setEditingEnabled(enabled : Boolean){
        allowEditing = enabled
        invalidateOptionsMenu()
    }
}
