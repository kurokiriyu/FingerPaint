package sample.application.fingerpaint;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

// �R���X�g���N�^�[
public class MemoDBHelper extends SQLiteOpenHelper {	// extends �́u�p���v�̈Ӗ��B

	// �N���X�ϐ�
	public static String name = "memos.db";
	public static CursorFactory factory = null;
	public static Integer version = 1;
	
	public MemoDBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO �����������ꂽ�R���X�g���N�^�[�E�X�^�u
	}

	public MemoDBHelper(Context context) {
		super(context, name, factory, version);
		// TODO �����������ꂽ�R���X�g���N�^�[�E�X�^�u
	}

	@Override
	public void onCreate(SQLiteDatabase db) { // onCreate�́u�C���X�^���X���\�b�h�i�߂�l�Ȃ��j�v�ł���B�J�n�Ƃ����Ӗ�������̂ł͂Ȃ��B
		String sql="CREATE TABLE memoDB (" // sql�́u���[�J���ϐ��v
				+android.provider.BaseColumns._ID
				+" INTEGER PRIMARY KEY AUTOINCREMENT, title Text, memo TEXT);";
		db.execSQL(sql); // �C���X�^���X���\�b�h�u�G�O�[�N�e�B�u�N���XSQL�v
		// TODO �����������ꂽ���\�b�h�E�X�^�u

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u

	}

}
