package com.SE.team12.pillalarm2021;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.SE.team12.pillalarm2021.MemoContract;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MemoActivity extends AppCompatActivity {
    private EditText mTitleEditText;
    private EditText mContentEditText;
    private Button imageup;
    private TextView tvDate;
    private ImageView imageView;
    private Button write;
    private long mMemoId = -1;
    static final int REQUEST_CODE = 1;
    Uri uri;
    String imgName = "osz.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_memo);
        tvDate = findViewById(R.id.date);
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        tvDate.setText("날짜 : "+sdf.format(d));

        imageView = findViewById(R.id.images);
        mTitleEditText = findViewById(R.id.title_edit);
        mContentEditText = findViewById(R.id.content_edit);
        imageup = findViewById(R.id.imageUp);


        imageup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 101);
            }
        });


        Intent intent = getIntent();
        if (intent != null) {
            mMemoId = intent.getLongExtra("id", -1);
            String title = intent.getStringExtra("title");
            String content = intent.getStringExtra("content");


            mTitleEditText.setText(title);
            mContentEditText.setText(content);

            try {
                String imgpath = getCacheDir() + "/" + imgName;
                Bitmap bm = BitmapFactory.decodeFile(imgpath);
                imageView.setImageBitmap(bm);
                Toast.makeText(getApplicationContext(), "파일 로드 성공", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "파일 로드 실패", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void setImage(Uri uri) {
        try{
            InputStream in = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(in);
            imageView.setImageBitmap(bitmap);
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { // 갤러리
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                Uri fileUri = data.getData();

                ContentResolver resolver = getContentResolver();
                try {
                    InputStream instream = resolver.openInputStream(fileUri);
                    Bitmap imgBitmap = BitmapFactory.decodeStream(instream);
                    imageView.setImageBitmap(imgBitmap);
                    instream.close();
                    saveBitmapToJpeg(imgBitmap);
                    Toast.makeText(getApplicationContext(), "파일 불러오기 성공", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "파일 불러오기 실패", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void saveBitmapToJpeg(Bitmap bitmap) {
        Intent intent = getIntent();
        String image = intent.getStringExtra("image");
        File tempFile = new File(getCacheDir(), imgName);
        try {
            tempFile.createNewFile();
            FileOutputStream out = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();
            Toast.makeText(getApplicationContext(), "파일 저장 성공", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "파일 저장 실패", Toast.LENGTH_SHORT).show();
        }
    }


    // back button press
    @Override
    public void onBackPressed() {
        String title = mTitleEditText.getText().toString();
        String contents = mContentEditText.getText().toString();

        // to dave to SQLite
        // contentValues.put(key, value)
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        ContentValues contentValues = new ContentValues();
        contentValues.put(MemoContract.MemoEntry.COLUMN_NAME_TITLE, sdf.format(d) + "에 작성한 메모입니다.");
        contentValues.put(MemoContract.MemoEntry.COLUMN_NAME_CONTENT, contents);

        // DB에 전달
        SQLiteDatabase db = MemoDbHelper.getsInstance(this).getWritableDatabase();

        if (mMemoId == -1) {
            long newRowID = db.insert(MemoContract.MemoEntry.TABLE_NAME,
                    null,
                    contentValues);

            if (newRowID == -1) {
                Toast.makeText(this, "메모 저장에 실패했습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "메모를 저장하였습니다.", Toast.LENGTH_SHORT).show();
                // content delivery
                setResult(RESULT_OK);
            }
        } else {
            // 내용이 수정이 되었다면
            // if content has been modified.
            int count = db.update(MemoContract.MemoEntry.TABLE_NAME, contentValues,
                    MemoContract.MemoEntry._ID + "=" + mMemoId, null);

            if (count == 0) {
                Toast.makeText(this, "메모 수정에 실패했습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "메모를 수정했습니다.", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
            }
        }

        super.onBackPressed();
    }
}