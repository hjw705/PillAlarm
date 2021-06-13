package com.SE.team12.pillalarm2021;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class memomain extends AppCompatActivity {
    public static final int REQUEST_CODE_INSERT = 1000;
    private MemoAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memo_write);

        FloatingActionButton add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change view
                startActivityForResult(new Intent(memomain.this, MemoActivity.class), REQUEST_CODE_INSERT);
            }
        });

        ListView listView = findViewById(R.id.memo_list);


        Cursor cursor = getMemoCursor();
        mAdapter = new MemoAdapter(this, cursor);
        listView.setAdapter((ListAdapter) mAdapter);

        // 리스트의 아이템을 클릭했을 때 내용이 보이게 익명 클래스
        // anonymous class to show content when you click an item is the list
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(memomain.this, MemoActivity.class);

                Cursor cursor = (Cursor) mAdapter.getItem(position);

                String title = cursor.getString(cursor.getColumnIndexOrThrow(MemoContract.MemoEntry.COLUMN_NAME_TITLE));
                String content = cursor.getString(cursor.getColumnIndexOrThrow(MemoContract.MemoEntry.COLUMN_NAME_CONTENT));

                intent.putExtra("id", id);
                intent.putExtra("title", title);
                intent.putExtra("content", content);


                startActivityForResult(intent, REQUEST_CODE_INSERT);
            }
        });

        // 리스트 아이템을 오래 누르고 있을때
        // -> 삭제기능
        // when long pressing the list item
        // -> delete function
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final long deleteId = id;

                AlertDialog.Builder builder = new AlertDialog.Builder(memomain.this);
                //builder.setTitle("메모 삭제");
                builder.setTitle("memo delete");
                //builder.setMessage("메모를 삭제하시겠습니다?");
                builder.setMessage("are you going to delete the note?");
                builder.setPositiveButton("삭제(delete)", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // DB 불러오기
                        // calling up DB
                        SQLiteDatabase db = MemoDbHelper.getsInstance(memomain.this).getWritableDatabase();
                        int deletedCount = db.delete(MemoContract.MemoEntry.TABLE_NAME,
                                MemoContract.MemoEntry._ID + "=" + deleteId, null);

                        if (deletedCount == 0 ) {
                            //Toast.makeText(MainActivity.this, "삭제에 문제가 발생하였습니다", Toast.LENGTH_SHORT).show();
                            Toast.makeText(memomain.this, "delete error", Toast.LENGTH_SHORT).show();
                        } else {
                            mAdapter.swapCursor(getMemoCursor());
                            //Toast.makeText(MainActivity.this, "삭제가 되었습니다.", Toast.LENGTH_SHORT).show();
                            Toast.makeText(memomain.this, "delete success", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                //builder.setNegativeButton("취소", null);
                builder.setNegativeButton("cancel", null);
                builder.show();

                return true;
            }
        });

    }

    // 디비에서 조회하는 메소드
    // method that is inquiry by db
    private Cursor getMemoCursor() {
        MemoDbHelper dbHelper = MemoDbHelper.getsInstance(this);
        return dbHelper.getReadableDatabase()
                .query(MemoContract.MemoEntry.TABLE_NAME,
                        null, null, null, null, null, null);
    }

    // startActivityForResult 이걸로 넘겨서 받는 메소드
    // startActivityForResult to receive data
    // 메모 작성 후 보여질때 최신내용으로 다시 보여짐
    // redisplay for the latest internal use when viewed after creating a note
    // -> swapCursor()
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_INSERT && resultCode == RESULT_OK) {
            mAdapter.swapCursor(getMemoCursor());
        }
    }

    // 리스트뷰에 내용을 뿌리기 위한 Adapter
    // Adapter to spray content in listView
    private static class MemoAdapter extends CursorAdapter {

        public MemoAdapter(Context context, Cursor c) {
            super(context, c);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            // 안드로이드에서 미리 제공해주는 레이아웃을 뿌린다.
            // show a layout that is provided in advance by android
            return LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        // 데이터를 실제로 뿌려주는 메소드
        /// a method that actually spills data
        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView titleText = view.findViewById(android.R.id.text1);
            titleText.setText(cursor.getString(cursor.getColumnIndexOrThrow(MemoContract.MemoEntry.COLUMN_NAME_TITLE)));
        }

    }
}
