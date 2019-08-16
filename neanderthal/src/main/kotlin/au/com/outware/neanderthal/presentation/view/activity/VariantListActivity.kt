package au.com.outware.neanderthal.presentation.view.activity

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
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

        val args = Bundle()
        intent?.extras?.let {
            args.putAll(intent.extras)
        }
        savedInstanceState?.let {
            args.putAll(it)
        }
        presenter.onCreate(args)

        variantList.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        variantList.addItemDecoration(DividerItemDecoration(this, android.R.drawable.divider_horizontal_bright))
        variantList.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?) = inflateMenu(R.menu.neanderthal_menu_variant_list, menu)

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.let {
            menu.findItem(R.id.neanderthal_menu_item_edit).isVisible = allowEditing
        }

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.neanderthal_menu_item_add -> presenter.onAddClicked()
            R.id.neanderthal_menu_item_edit -> presenter.onEditClicked()
            R.id.neanderthal_menu_item_launch_application -> presenter.onLaunchClicked()
            R.id.neanderthal_menu_item_reset -> presenter.onResetToDefaultClicked()
        }

        return super.onOptionsItemSelected(item)
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
        // Updating the logic to restart the app as the prior got depricated with Android Q
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // In case we are called with non-Activity context.
        startActivity(launchIntent);
        finish();
        Runtime.getRuntime().exit(0);
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
        Toast.makeText(this, R.string.neanderthal_delete_notice_action, Toast.LENGTH_SHORT).show()
    }

    override fun notifyReset() {
        Toast.makeText(this, R.string.neanderthal_reset_notice_message, Toast.LENGTH_SHORT).show()
    }

    override fun setEditingEnabled(enabled: Boolean){
        allowEditing = enabled
        invalidateOptionsMenu()
    }
}
