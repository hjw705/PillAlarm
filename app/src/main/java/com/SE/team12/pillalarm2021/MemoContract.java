package com.SE.team12.pillalarm2021;

import android.provider.BaseColumns;

// 테이블 정보를 담을 계약 클래스
// Contracy class to hold table information
public class MemoContract {

    private MemoContract() {}

    // BaseColumns 인터페이스는 _ID 및 _COUNT 열의 이름을 제공
    // The BaseColumns interface provides the names of the
    // _ID and _COUNT columns.
    public static class MemoEntry implements BaseColumns {
        public static final String TABLE_NAME = "memo";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_CONTENT = "content";
        public static final String COLUMN_NAME_IMAGE = "image";
    }
}