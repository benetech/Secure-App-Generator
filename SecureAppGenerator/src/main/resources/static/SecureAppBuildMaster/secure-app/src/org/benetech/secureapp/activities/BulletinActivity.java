package org.benetech.secureapp.activities;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import org.benetech.secureapp.tasks.ZipBulletinTask;
import org.javarosa.form.api.FormEntryController;
import org.benetech.secureapp.collect.io.SecureFileStorageManager;
import org.benetech.secureapp.collect.tasks.SecureFormLoaderTask;
import org.martus.android.library.common.dialog.DeterminateProgressDialog;
import org.martus.android.library.common.dialog.IndeterminateProgressDialog;
import org.martus.android.library.exceptions.XFormsConstraintViolationException;
import org.martus.android.library.exceptions.XFormsMissingRequiredFieldException;
import org.martus.android.library.io.SecureFile;
import org.martus.android.library.utilities.BulletinSender;
import org.benetech.secureapp.utilities.OdkFormInstanceValidator;
import org.benetech.secureapp.utilities.Utility;
import org.benetech.secureapp.FormFromAssetFolderExtractor;
import org.benetech.secureapp.MartusUploadManager;
import org.benetech.secureapp.R;
import org.benetech.secureapp.application.AppConfig;
import org.benetech.secureapp.application.MainApplication;
import org.benetech.secureapp.clientside.SecureMobileAttachmentProxy;
import org.benetech.secureapp.tasks.UploadBulletinTask;
import org.benetech.secureapp.clientside.SecureMobileClientBulletinStore;
import org.martus.common.HeadquartersKey;
import org.martus.common.HeadquartersKeys;
import org.martus.common.MartusXml;
import org.martus.common.MiniLocalization;
import org.martus.common.bulletin.Bulletin;
import org.martus.common.bulletin.BulletinZipUtilities;
import org.martus.common.crypto.MartusKeyPair;
import org.martus.common.crypto.MartusSecurity;
import org.martus.common.crypto.MobileMartusSecurity;
import org.martus.common.network.NetworkInterfaceConstants;
import org.martus.common.packet.UniversalId;
import org.martus.util.MultiCalendar;
import org.martus.util.xml.XmlUtilities;
import org.odk.collect.android.listeners.FormLoaderListener;
import org.odk.collect.android.provider.FormsProviderAPI;
import org.odk.collect.android.provider.InstanceProviderAPI;
import org.odk.collect.android.tasks.FormLoaderTask;

import java.io.File;
import java.util.ArrayList;
import java.util.zip.ZipFile;

/**
 * @author roms Date: 10/25/12
 *         animal@martus.org 9/20/2014
 */

public class BulletinActivity extends SherlockFragmentActivity implements BulletinSender, IndeterminateProgressDialog.IndeterminateProgressDialogListener,
        DeterminateProgressDialog.DeterminateProgressDialogListener,
        FormLoaderListener{

    private static final String TAG = "BulletinActivity";
    private SecureMobileClientBulletinStore store;
    private IndeterminateProgressDialog indeterminateDialog;
    private DeterminateProgressDialog determinateDialog;
    private HeadquartersKey hqKey;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        store = AppConfig.getInstance(getApplication()).getStore();

        hqKey = new HeadquartersKey(getDesktopPublicKey());

        SecureFileStorageManager secureFileStorageManager = ((MainApplication) getApplication()).getMountedSecureStorage();
        Cursor cursor = getApplication().getContentResolver().query(FormsProviderAPI.FormsColumns.CONTENT_URI, null, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(FormsProviderAPI.FormsColumns.FORM_FILE_PATH);
        String formPath = cursor.getString(columnIndex);
        try {

            indeterminateDialog = IndeterminateProgressDialog.newInstance();
            indeterminateDialog.show(getSupportFragmentManager(), "dlg_zipping");

            String instancePath = getIntent().getExtras().getString(MartusUploadManager.BULLETIN_ISTANCE_FILE_PATH_TAG);
            SecureFormLoaderTask task = new SecureFormLoaderTask(secureFileStorageManager, instancePath, null, null);
            task.setFormLoaderListener(this);
            task.execute(formPath);

        } catch (Exception e) {
            Log.e(TAG, getString(R.string.error_message_could_not_create_record), e);
            indeterminateDialog.dismissAllowingStateLoss();
            finish();
            Toast.makeText(this, getString(R.string.loading_form), Toast.LENGTH_LONG).show();
        }
    }

    private String getDesktopPublicKey() {
        return getString(R.string.public_key_desktop);
    }

    private Bulletin createBulletin() throws Exception {
        store.doAfterSigninInitialization(getAppDir());
        Bulletin bulletin = store.createEmptyBulletin();

        bulletin.setAuthorizedToReadKeys(new HeadquartersKeys(hqKey));
        bulletin.setMutable();
        bulletin.changeState(Bulletin.BulletinState.STATE_SHARED);
        bulletin.setAllPrivate(true);

        String bulletin_display_name = getIntent().getExtras().getString(MartusUploadManager.BULLETIN_DISPLAY_NAME_TAG);
        String authorName = getIntent().getExtras().getString(MartusUploadManager.BULLETIN_AUTHOR_TAG);
        String organizationName = getIntent().getExtras().getString(MartusUploadManager.BULLETIN_ORGANIZATION_TAG);

        bulletin.set(Bulletin.TAGLANGUAGE, MiniLocalization.ENGLISH);
        bulletin.set(Bulletin.TAGENTRYDATE, new MultiCalendar().toString());
        bulletin.set(Bulletin.TAGTITLE, bulletin_display_name);
        bulletin.set(Bulletin.TAGAUTHOR, authorName);
        bulletin.set(Bulletin.TAGORGANIZATION, organizationName);

        addXFormsElement(bulletin);

        return bulletin;
    }

    private void addXFormsElement(Bulletin bulletin) throws Exception {
        String formInstance = getXFormsInstanceContent(this);
        if (formInstance == null)
            throw new RuntimeException(getString(R.string.error_message_failed_to_load_form_model_or_instance));

        String xFormsModelAsString = getXFormsModelAsString();
        validateInstanceForm(xFormsModelAsString, formInstance);
        String xFormsModelAsStringToUse = createXmlChunck(MartusXml.XFormsModelElementName, xFormsModelAsString);
        bulletin.getFieldDataPacket().setXFormsModelAsString(xFormsModelAsStringToUse);

        String xFormsInstanceAsStringToUse = createXmlChunck(MartusXml.XFormsInstanceElementName, formInstance);
        bulletin.getFieldDataPacket().setXFormsInstanceAsString(xFormsInstanceAsStringToUse);
    }

    private void validateInstanceForm(String xFormsModelAsString, String formInstance) throws Exception {
        OdkFormInstanceValidator validator = new OdkFormInstanceValidator(xFormsModelAsString, formInstance);
        final int singleValidationOutcomeCode = validator.validateUserAnswersOneAtATime();
        if (singleValidationOutcomeCode == FormEntryController.ANSWER_CONSTRAINT_VIOLATED)
            throw new XFormsConstraintViolationException();

        if (singleValidationOutcomeCode == FormEntryController.ANSWER_REQUIRED_BUT_EMPTY)
            throw new XFormsMissingRequiredFieldException();
    }

    private String createXmlChunck(String elementName, String xmlAsString) {
        StringBuilder elementBuilder = new StringBuilder();
        elementBuilder.append(XmlUtilities.createStartElement(elementName));
        xmlAsString = XmlUtilities.stripXmlHeader(xmlAsString);
        elementBuilder.append(xmlAsString);
        elementBuilder.append(XmlUtilities.createEndTag(elementName));

        return elementBuilder.toString();
    }

    private String getXFormsModelAsString() throws Exception {
        return new FormFromAssetFolderExtractor((MainApplication)getApplication()).getXFormsModelAsString();
    }

    private void zipAndUploadBulletin(Bulletin bulletin)  {
        indeterminateDialog = IndeterminateProgressDialog.newInstance();
        indeterminateDialog.show(getSupportFragmentManager(), "dlg_zipping");

        //turn off user inactivity checking during zipping and encrypting of file
        final AsyncTask<Object, Integer, File> zipTask = new ZipBulletinTask(bulletin, this);
        zipTask.execute(getApplication().getCacheDir(), store);
    }

    @Override
    public void onZipped(Bulletin bulletin, File zippedFile) {
        try {
            ZipFile zipFile = new ZipFile(zippedFile);
            BulletinZipUtilities.validateIntegrityOfZipFilePackets(store.getAccountId(), zipFile, getSecurity());
        } catch (Exception e) {
            Log.e(TAG, getString(R.string.error_message_error_verifying_zip_file), e);
            indeterminateDialog.dismissAllowingStateLoss();
            Toast.makeText(this, getString(R.string.failure_zipping_bulletin), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if (zippedFile == null) {
            Toast.makeText(this, getString(R.string.failure_zipping_bulletin), Toast.LENGTH_SHORT).show();
            return;
        }

        uploadBulletin(bulletin, zippedFile);
    }

    private void uploadBulletin(Bulletin bulletin, File zippedFile) {
        determinateDialog = DeterminateProgressDialog.newInstance();
        try {
            determinateDialog.show(getSupportFragmentManager(), "dlg_sending");
        } catch (IllegalStateException e) {
            determinateDialog.dismissAllowingStateLoss();
            // just means user has left app - do nothing
        }

        UniversalId bulletinId = bulletin.getUniversalId();
        AsyncTask<Object, Integer, String> uploadTask = new UploadBulletinTask(getApplication(), this, bulletinId);
        MartusSecurity cryptoCopy = cloneSecurity(getSecurity());
        uploadTask.execute(bulletin.getUniversalId(), zippedFile, AppConfig.getInstance(getApplication()).getCurrentNetworkInterfaceGateway(getServerIp(), getServerPublicKey()), cryptoCopy);
    }

    private MartusSecurity getSecurity() {
        return AppConfig.getInstance(getApplication()).getCrypto();
    }

    @Override
    public void onSent(String result) {
        try {
            determinateDialog.dismissAllowingStateLoss();
        } catch (IllegalStateException e) {
            //this is okay as the user may have closed this screen
        }

        if (result != null && result.equals(NetworkInterfaceConstants.OK))
            deleteFormAfterSent();

        String message = getResultMessage(result, this);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        finish();
    }

    private void deleteFormAfterSent() {
        String instancePath = getIntent().getExtras().getString(MartusUploadManager.BULLETIN_ISTANCE_FILE_PATH_TAG);
        SecureFile secureFileToDelete = new SecureFile(instancePath);
        boolean wasDeleted = secureFileToDelete.delete();
        if (wasDeleted)
            Log.i(TAG, getString(R.string.error_message_form_was_deleted_after_upload_was_completed));
        else
            Log.i(TAG, getString(R.string.error_message_form_could_not_be_deleted));

        MainActivity.deleteOdkInstanceCacheDir(instancePath);
        removeFormFromOdkDatabase();
    }

    private void removeFormFromOdkDatabase() {
        String formId = getIntent().getExtras().getString(MartusUploadManager.BULLETIN_FORM_ID_TAG);
        Cursor cursor = getContentResolver().query(InstanceProviderAPI.InstanceColumns.CONTENT_URI, null, null, null, null);
        cursor.moveToFirst();
        int deleteCount = getContentResolver().delete(InstanceProviderAPI.InstanceColumns.CONTENT_URI,
                InstanceProviderAPI.InstanceColumns._ID + " = ?",
                new String[] { formId });

        Log.i(TAG,  getString(R.string.error_message_instance_form_deleted_delete_count, deleteCount));
        cursor.close();
    }

    @Override
    public void onProgressUpdate(int progress) {
        if (null != determinateDialog.getProgressDialog()) {
            determinateDialog.getProgressDialog().setProgress(progress);
        }
    }
    public static String getResultMessage(String result, Context context) {
        String message;
        if (result != null && result.equals(NetworkInterfaceConstants.OK)) {
            message = context.getString(R.string.successful_send_notification);
        } else {
            message = context.getString(R.string.failed_send_notification, result);
        }
        return message;
    }

    private MartusSecurity cloneSecurity(MartusSecurity original) {
        MartusSecurity cryptoCopy = null;
        try {
            MartusKeyPair keyPair = original.getKeyPair();
            byte[] data = keyPair.getKeyPairData();
            cryptoCopy = new MobileMartusSecurity();
            cryptoCopy.setKeyPairFromData(data);
            cryptoCopy.setShouldWriteAuthorDecryptableData(false);
        } catch (Exception e) {
            Log.e(TAG, getString(R.string.error_message_problem_copying_crypto), e);
        }
        return cryptoCopy;
    }

    private SecureFile getAppDir() {
        return ((MainApplication)getApplication()).getSecureStorageDir();
    }

    @Override
    public String getIndeterminateDialogMessage() {
        return getString(R.string.preparing_record_for_upload);
    }

    @Override
    public String getDeterminateDialogMessage() {
        return getString(R.string.uploading_record);
    }

    @Override
    public void onDeterminateDialogCancel() {
    }

    @Override
    public void loadingComplete(FormLoaderTask task) {
        try {
            Bulletin bulletin = createBulletin();
            addAttachmentsToBulletin(bulletin);
            zipAndUploadBulletin(bulletin);
        } catch (XFormsConstraintViolationException e) {
            handleException(e, R.string.xforms_constraint_error, getString(R.string.error_message_forms_constraint_issue_during_validation));
        } catch (XFormsMissingRequiredFieldException e) {
            handleException(e, R.string.xforms_missing_required_field, getString(R.string.error_message_xforms_required_fields_missing));
        } catch (Exception e){
            handleException(e, R.string.failure_zipping_bulletin, getString(R.string.error_message_exception_thrown_trying_to_populate_record));
            Log.e(TAG, getString(R.string.error_message_exception_thrown_trying_to_populate_record), e);
        }
    }

    private void handleException(Exception e, int id, String msg) {
        indeterminateDialog.dismissAllowingStateLoss();
        finish();
        Toast.makeText(this, getString(id), Toast.LENGTH_LONG).show();
        Log.w(TAG, msg, e);
    }

    private String getXFormsInstanceContent(Context context) throws Exception{
        String formInstancePath = null;
        try {
            formInstancePath = findInstanceFormPath(context);
            if (formInstancePath == null)
                return null;
            SecureFile file = new SecureFile(formInstancePath);

            return Utility.ioCipherFileToString(file);
        }
        catch (Exception e) {
            Log.e(TAG, getString(R.string.error_message_error_reading_file_path, formInstancePath), e);
            return null;
        }
    }

    private String findInstanceFormPath(Context context) {
        Cursor cursor = context.getContentResolver().query(InstanceProviderAPI.InstanceColumns.CONTENT_URI, null, null, null, null);
        String displayNameToMatch = getIntent().getExtras().getString(MartusUploadManager.BULLETIN_DISPLAY_NAME_TAG);
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
        {
            int nameColumnIndex = cursor.getColumnIndex(InstanceProviderAPI.InstanceColumns.DISPLAY_NAME);
            String name = cursor.getString(nameColumnIndex);

            if (name.equals(displayNameToMatch)) {
                int formInstancePathColumnIndex = cursor.getColumnIndex(InstanceProviderAPI.InstanceColumns.INSTANCE_FILE_PATH);
                String formInstancePath = cursor.getString(formInstancePathColumnIndex);

                return formInstancePath;
            }
        }

        return null;
    }

    @Override
    public void loadingError(String errorMsg) {
        Log.e(TAG, errorMsg);
    }

    @Override
    public void onProgressStep(String stepMessage) {
    }

    private void addAttachmentsToBulletin(Bulletin bulletin) throws Exception
    {
        ArrayList<SecureFile> attachmentFiles = getJpegFiles();
        for (SecureFile attachmentFile : attachmentFiles)
        {
            SecureMobileAttachmentProxy attachementProxy = new SecureMobileAttachmentProxy(attachmentFile);
            if (attachmentFile.exists())
                bulletin.addPublicAttachment(attachementProxy);
            else
                throw new Exception(getString(R.string.error_message_file_to_attach_does_not_exist, attachmentFile));
        }
    }

    private ArrayList<SecureFile> getJpegFiles()
    {
        String formInstancePath = findInstanceFormPath(this);
        if (formInstancePath == null)
            return new ArrayList();

        SecureFile attachmentsDir = ManageAttachmentsActivty.getAttachmentsDir(formInstancePath);
        SecureFile[] jpegFilesInInstanceDir = attachmentsDir.listFiles();
        ArrayList<SecureFile> jpegFiles = new ArrayList();
        for (int index = 0; index < jpegFilesInInstanceDir.length; ++index)
        {
            SecureFile jpegFile = jpegFilesInInstanceDir[index];
            jpegFiles.add(jpegFile);
        }

        return  jpegFiles;
    }

    private String getServerIp(){
        return getString(R.string.martus_server_ip);
    }

    private String getServerPublicKey() {
        return getString(R.string.martus_server_public_key);
    }
}