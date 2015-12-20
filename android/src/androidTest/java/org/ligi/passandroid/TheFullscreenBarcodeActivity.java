package org.ligi.passandroid;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.squareup.spoon.Spoon;

import org.ligi.passandroid.helper.BarcodeDecoder;
import org.ligi.passandroid.model.BarCode;
import org.ligi.passandroid.model.PassImpl;
import org.ligi.passandroid.model.PassStore;
import org.ligi.passandroid.ui.FullscreenBarcodeActivity;
import org.ligi.tracedroid.TraceDroid;

import java.util.UUID;

import javax.inject.Inject;

import butterknife.ButterKnife;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.assertj.core.api.Assertions.assertThat;

public class TheFullscreenBarcodeActivity extends BaseIntegration<FullscreenBarcodeActivity> {

    @Inject
    PassStore passStore;

    public static final String BARCODE_MESSAGE = "2323";

    public TheFullscreenBarcodeActivity() {
        super(FullscreenBarcodeActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        TestComponent component = DaggerTestComponent.create();

        component.inject(this);

        App.setComponent(component);

        TraceDroid.deleteStacktraceFiles();
    }

    @MediumTest
    public void testPDF417BarcodeIsShown() {
        testWithBarcodeFormat(BarcodeFormat.PDF_417);

        Spoon.screenshot(getActivity(), "pdf417_barcode");
    }


    @MediumTest
    public void testAztecBarcodeIsShown() {
        testWithBarcodeFormat(BarcodeFormat.AZTEC);

        Spoon.screenshot(getActivity(), "aztec_barcode");
    }


    @MediumTest
    public void testQRCodeIsShown() {
        testWithBarcodeFormat(BarcodeFormat.QR_CODE);

        Spoon.screenshot(getActivity(), "qr_barcode");
    }

    @MediumTest
    public void testCode128CodeIsShown() {
        testWithBarcodeFormat(BarcodeFormat.CODE_128);

        Spoon.screenshot(getActivity(), "code128_barcode");
    }


    @MediumTest
    public void testCode39CodeIsShown() {
        testWithBarcodeFormat(BarcodeFormat.CODE_39);

        Spoon.screenshot(getActivity(), "code39_barcode");
    }



    private void testWithBarcodeFormat(final BarcodeFormat format) {
        final PassImpl pass = new PassImpl(UUID.randomUUID().toString());
        pass.setBarCode(new BarCode(format, BARCODE_MESSAGE));

        passStore.setCurrentPass(pass);
        getActivity();
        onView(withId(R.id.fullscreen_barcode)).check(matches(isDisplayed()));

        final ImageView viewById = ButterKnife.findById(getActivity(), R.id.fullscreen_barcode);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) viewById.getDrawable();
        final Bitmap bitmap = bitmapDrawable.getBitmap();

        final Bitmap bitmapToTest;
        if (format == BarcodeFormat.AZTEC) {
            // not sure why - but for the decoder to pick up AZTEC it must have moar pixelz - smells like a zxing bug
            bitmapToTest = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() * 2, bitmap.getHeight() * 2, false);
        } else {
            bitmapToTest = bitmap;
        }

        assertThat(BarcodeDecoder.decodeBitmap(bitmapToTest)).isEqualTo(BARCODE_MESSAGE);
    }

}
