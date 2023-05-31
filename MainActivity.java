package org.pytorch.helloworld;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.pytorch.IValue;
import org.pytorch.LiteModuleLoader;
import org.pytorch.Module;
import org.pytorch.Tensor;
import org.pytorch.torchvision.TensorImageUtils;
import org.pytorch.MemoryFormat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  Module module = null;
  Bitmap bitmap = null;
  int bitmap_index = 1;
  private ImageView imageView;
  private TextView textView;
  private ArrayList<String> classNames;



  public static void closeStrictMode() {
    StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
            .detectAll().penaltyLog().build());
  }
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    try {

      module = LiteModuleLoader.load(assetFilePath(this, "Mobilevit.pt"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    // 获取控件
    imageView = findViewById(R.id.image);
    textView = findViewById(R.id.text);
    Button selectImgBtn = findViewById(R.id.select_img_btn);
    Button openCamera = findViewById(R.id.open_camera);
    selectImgBtn.setOnClickListener(this);
    openCamera.setOnClickListener(this);
  }
  @SuppressLint("NonConstantResourceId")
  public void onClick(View v){
    switch(v.getId()){
      case R.id.select_img_btn:
        Intent chooseIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooseIntent.setType("image/*");
        chooseIntent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(chooseIntent, 1);
        break;
      case R.id.open_camera:
        Intent intent = new Intent(MainActivity.this, CameraActivity.class);
        startActivity(intent);
        break;
    }
  }

  protected void onActivityResult(int requestcode, int resultcode, Intent data){
    super.onActivityResult(requestcode, resultcode, data);
    if(requestcode == 1){
      try {
        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());

      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      imageView.setImageBitmap(bitmap);
      final Tensor inputTensor = TensorImageUtils.bitmapToFloat32Tensor(bitmap,
              TensorImageUtils.TORCHVISION_NORM_MEAN_RGB, TensorImageUtils.TORCHVISION_NORM_STD_RGB, MemoryFormat.CHANNELS_LAST);
      final Tensor outputTensor = module.forward(IValue.from(inputTensor)).toTensor();
      final float[] scores = outputTensor.getDataAsFloatArray();
      float maxScore = -Float.MAX_VALUE;
      int maxScoreIdx = -1;
      for (int i = 0; i < scores.length; i++) {
        if (scores[i] > maxScore) {
          maxScore = scores[i];
          maxScoreIdx = i;
        }
      }
      String className = ImageNetClasses.IMAGENET_CLASSES[maxScoreIdx];
      TextView textView = findViewById(R.id.text);
      textView.setText(className);
    }
  }


  public static String assetFilePath(Context context, String assetName) throws IOException {
    File file = new File(context.getFilesDir(), assetName);
    if (file.exists() && file.length() > 0) {
      return file.getAbsolutePath();
    }

    try (InputStream is = context.getAssets().open(assetName)) {
      try (OutputStream os = new FileOutputStream(file)) {
        byte[] buffer = new byte[4 * 1024];
        int read;
        while ((read = is.read(buffer)) != -1) {
          os.write(buffer, 0, read);
        }
        os.flush();
      }
      return file.getAbsolutePath();
    }
  }
}
