package com.example.manu.myapplication.BD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Manu on 27/04/2016.
 */
public class DataBaseHelper extends SQLiteOpenHelper{
    static final String dbName = "DB_EASYREST";

    static final String CATEGORIAS_TABLE = "CATEGORIAS";
    static final String COL_ID_CATEGORIA = "ID_CATEGORIA";
    static final String COL_NOMBRE_CATEGORIA = "NOMBRE_CATEGORIA";
    static final String COL_IMAGEN_CATEGORIA = "IMAGEN_CATEGORIA";


    static final String INSUMOS_MENUS_TABLE = "INSUMOS";
    static final String COL_ID_INSUMO = "ID_INSUMO";
    static final String COL_NOMBRE_INSUMO = "NOMBRE_INSUMO";
    static final String COL_DESCRIPCION_INSUMO = "DESCRIPCION_INSUMO";
    static final String COL_STOCK_INSUMO = "STOCK_INSUMO";
    static final String COL_PRECIO_INSUMO = "PRECIO_INSUMO";
    static final String COL_IMAGEN_INSUMO = "IMAGEN_INSUMO";
    static final String COL_ID_CATEGORIA_INSUMO = "ID_CATEGORIA";




    public DataBaseHelper(Context context) {
        super(context, dbName, null, 1);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + CATEGORIAS_TABLE + " ("
                + COL_ID_CATEGORIA + " INTEGER PRIMARY KEY , "
                + COL_NOMBRE_CATEGORIA + " TEXT , "
                + COL_IMAGEN_CATEGORIA + " TEXT)");

        db.execSQL("CREATE TABLE " + INSUMOS_MENUS_TABLE + " ("
                + COL_ID_INSUMO + " INTEGER PRIMARY KEY, "
                + COL_NOMBRE_INSUMO + " TEXT , "
                + COL_DESCRIPCION_INSUMO + " TEXT , "
                + COL_STOCK_INSUMO + " INTEGER , "
                + COL_PRECIO_INSUMO + " REAL , "
                + COL_IMAGEN_INSUMO + " TEXT , "
                + COL_ID_CATEGORIA_INSUMO + " INTEGER , "
                + " FOREIGN KEY (" + COL_ID_CATEGORIA_INSUMO
                + ") REFERENCES " + CATEGORIAS_TABLE + " (" + COL_ID_CATEGORIA + "));");


/*
        db.execSQL("CREATE TRIGGER fk_menuXMesa_Menu " + " BEFORE INSERT "
                + " ON " + menuXMesaTable +
                " FOR EACH ROW BEGIN" + " SELECT CASE WHEN ((SELECT "
                + colIdMenu + " FROM " + menuTable + " WHERE " + colIdMenu
                + "=new." + colNumeroMenu + " ) IS NULL)"
                + " THEN RAISE (ABORT,'Foreign Key Violation') END;" + "  END;");
*/

        insertarCategorias(db);
        insertarInsumos(db);






    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + INSUMOS_MENUS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CATEGORIAS_TABLE);
       // db.execSQL("DROP TRIGGER IF EXISTS fk_menuXMesa_Menu");
        onCreate(db);

    }



    void insertarCategorias(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(COL_ID_CATEGORIA, 1);
        cv.put(COL_NOMBRE_CATEGORIA, "MINUTAS");
        cv.put(COL_IMAGEN_CATEGORIA, "MINUTAS");
        db.insert(CATEGORIAS_TABLE, COL_ID_CATEGORIA, cv);

        cv.put(COL_ID_CATEGORIA, 2);
        cv.put(COL_NOMBRE_CATEGORIA, "LOMITOS");
        cv.put(COL_IMAGEN_CATEGORIA, "LOMITOS");
        db.insert(CATEGORIAS_TABLE, COL_ID_CATEGORIA, cv);

        cv.put(COL_ID_CATEGORIA, 3);
        cv.put(COL_NOMBRE_CATEGORIA, "PASTAS");
        cv.put(COL_IMAGEN_CATEGORIA, "PASTAS");
        db.insert(CATEGORIAS_TABLE, COL_ID_CATEGORIA, cv);

        cv.put(COL_ID_CATEGORIA, 4);
        cv.put(COL_NOMBRE_CATEGORIA, "TABLAS");
        cv.put(COL_IMAGEN_CATEGORIA, "TABLAS");
        db.insert(CATEGORIAS_TABLE, COL_ID_CATEGORIA, cv);

        cv.put(COL_ID_CATEGORIA, 5);
        cv.put(COL_NOMBRE_CATEGORIA, "BEBIDAS");
        cv.put(COL_IMAGEN_CATEGORIA, "BEBIDAS");
        db.insert(CATEGORIAS_TABLE, COL_ID_CATEGORIA, cv);

        cv.put(COL_ID_CATEGORIA, 6);
        cv.put(COL_NOMBRE_CATEGORIA, "PIZZAS");
        cv.put(COL_IMAGEN_CATEGORIA, "PIZZAS");
        db.insert(CATEGORIAS_TABLE, COL_ID_CATEGORIA, cv);

        cv.put(COL_ID_CATEGORIA, 7);
        cv.put(COL_NOMBRE_CATEGORIA, "POSTRES");
        cv.put(COL_IMAGEN_CATEGORIA, "POSTRES");
        db.insert(CATEGORIAS_TABLE, COL_ID_CATEGORIA, cv);

        cv.put(COL_ID_CATEGORIA, 8);
        cv.put(COL_NOMBRE_CATEGORIA, "DESAYUNO");
        cv.put(COL_IMAGEN_CATEGORIA, "DESAYUNO");
        db.insert(CATEGORIAS_TABLE, COL_ID_CATEGORIA, cv);

    }


    void insertarInsumos(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(COL_ID_INSUMO, 1);
        cv.put(COL_NOMBRE_INSUMO, "COCA COLA 1L");
        cv.put(COL_DESCRIPCION_INSUMO, "BEBIDA DE COLA COCA");
        cv.put(COL_IMAGEN_INSUMO, "IMAGEN_INSUMO");
        cv.put(COL_PRECIO_INSUMO, 25.5);
        cv.put(COL_STOCK_INSUMO, 20);
        cv.put(COL_ID_CATEGORIA_INSUMO, 5);
        db.insert(INSUMOS_MENUS_TABLE, COL_ID_INSUMO, cv);

        cv.put(COL_ID_INSUMO, 2);
        cv.put(COL_NOMBRE_INSUMO, "PEPSI COLA 1L");
        cv.put(COL_DESCRIPCION_INSUMO, "BEBIDA DE COLA PEPSI");
        cv.put(COL_IMAGEN_INSUMO, "IMAGEN_INSUMO");
        cv.put(COL_PRECIO_INSUMO, 24.5);
        cv.put(COL_STOCK_INSUMO, 15);
        cv.put(COL_ID_CATEGORIA_INSUMO, 5);
        db.insert(INSUMOS_MENUS_TABLE, COL_ID_INSUMO, cv);

        cv.put(COL_ID_INSUMO, 3);
        cv.put(COL_NOMBRE_INSUMO, "MILANESA NAPOLITANA CON PURÉ");
        cv.put(COL_DESCRIPCION_INSUMO, "MILANESA DE CARNE A LA NAPOLITANA CON PURÉ DE PAPA");
        cv.put(COL_IMAGEN_INSUMO, "IMAGEN_INSUMO");
        cv.put(COL_PRECIO_INSUMO, 128);
       // cv.put(COL_STOCK_INSUMO, 10);
        cv.put(COL_ID_CATEGORIA_INSUMO, 1);
        db.insert(INSUMOS_MENUS_TABLE, COL_ID_INSUMO, cv);

        cv.put(COL_ID_INSUMO, 4);
        cv.put(COL_NOMBRE_INSUMO, "SUPREMA NAPOLITANA CON PURÉ");
        cv.put(COL_DESCRIPCION_INSUMO, "MILANESA DE POLLO A LA NAPOLITANA CON PURÉ DE PAPA");
        cv.put(COL_IMAGEN_INSUMO, "IMAGEN_INSUMO");
        cv.put(COL_PRECIO_INSUMO, 115);
        // cv.put(COL_STOCK_INSUMO, 10);
        cv.put(COL_ID_CATEGORIA_INSUMO, 1);
        db.insert(INSUMOS_MENUS_TABLE, COL_ID_INSUMO, cv);


        cv.put(COL_ID_INSUMO, 5);
        cv.put(COL_NOMBRE_INSUMO, "LOMO SIMPLE");
        cv.put(COL_DESCRIPCION_INSUMO, "LOMO CON CARNE DE VACA, LECHUGA, TOMATE, MAYONESA Y PAPAS");
        cv.put(COL_IMAGEN_INSUMO, "IMAGEN_INSUMO");
        cv.put(COL_PRECIO_INSUMO, 110);
        // cv.put(COL_STOCK_INSUMO, 10);
        cv.put(COL_ID_CATEGORIA_INSUMO, 2);
        db.insert(INSUMOS_MENUS_TABLE, COL_ID_INSUMO, cv);

        cv.put(COL_ID_INSUMO, 6);
        cv.put(COL_NOMBRE_INSUMO, "LOMO COMPLETO");
        cv.put(COL_DESCRIPCION_INSUMO, "LOMO CON CARNE DE VACA, LECHUGA, TOMATE, QUESO, HUEVO, JAMON, MAYONESA Y PAPAS");
        cv.put(COL_IMAGEN_INSUMO, "IMAGEN_INSUMO");
        cv.put(COL_PRECIO_INSUMO, 130);
        // cv.put(COL_STOCK_INSUMO, 10);
        cv.put(COL_ID_CATEGORIA_INSUMO, 2);
        db.insert(INSUMOS_MENUS_TABLE, COL_ID_INSUMO, cv);

        cv.put(COL_ID_INSUMO, 7);
        cv.put(COL_NOMBRE_INSUMO, "ÑOQUIS CASEROS CON SALSA");
        cv.put(COL_DESCRIPCION_INSUMO, "ÑOQUIS DE PAPA CON SALSA ROJA");
        cv.put(COL_IMAGEN_INSUMO, "IMAGEN_INSUMO");
        cv.put(COL_PRECIO_INSUMO, 98);
        // cv.put(COL_STOCK_INSUMO, 10);
        cv.put(COL_ID_CATEGORIA_INSUMO, 3);
        db.insert(INSUMOS_MENUS_TABLE, COL_ID_INSUMO, cv);

        cv.put(COL_ID_INSUMO, 8);
        cv.put(COL_NOMBRE_INSUMO, "RAVIOLES CON SALSA BOLOGNESA");
        cv.put(COL_DESCRIPCION_INSUMO, "RAVIOLES DE JAMON Y QUESO O VERDURA CON SALSA CON CARNE MOLIDA");
        cv.put(COL_IMAGEN_INSUMO, "IMAGEN_INSUMO");
        cv.put(COL_PRECIO_INSUMO, 110);
        // cv.put(COL_STOCK_INSUMO, 10);
        cv.put(COL_ID_CATEGORIA_INSUMO, 3);
        db.insert(INSUMOS_MENUS_TABLE, COL_ID_INSUMO, cv);

        cv.put(COL_ID_INSUMO, 9);
        cv.put(COL_NOMBRE_INSUMO, "TABLA DE FIAMBRES P/2 PERSONAS");
        cv.put(COL_DESCRIPCION_INSUMO, "TABLA CON JAMON, QUESO DEL CAMPO, SALAME DE COLONIA, QUESO ROQUEFORT, JAMON CRUDO, ACEITUNAS, MILANESAS PICADAS.");
        cv.put(COL_IMAGEN_INSUMO, "IMAGEN_INSUMO");
        cv.put(COL_PRECIO_INSUMO, 160);
        // cv.put(COL_STOCK_INSUMO, 10);
        cv.put(COL_ID_CATEGORIA_INSUMO, 4);
        db.insert(INSUMOS_MENUS_TABLE, COL_ID_INSUMO, cv);

        cv.put(COL_ID_INSUMO, 10);
        cv.put(COL_NOMBRE_INSUMO, "TABLA ARGENTINA P/2 PERSONAS");
        cv.put(COL_DESCRIPCION_INSUMO, "TABLA CON MATAMBRE DE CERDO, CHORIZOS, MORCILLA, VACIO, POLLO, CHINCHULINES Y MOLLEJAS.");
        cv.put(COL_IMAGEN_INSUMO, "IMAGEN_INSUMO");
        cv.put(COL_PRECIO_INSUMO, 240);
        // cv.put(COL_STOCK_INSUMO, 10);
        cv.put(COL_ID_CATEGORIA_INSUMO, 4);
        db.insert(INSUMOS_MENUS_TABLE, COL_ID_INSUMO, cv);

        cv.put(COL_ID_INSUMO, 11);
        cv.put(COL_NOMBRE_INSUMO, "PIZZA NAPOLITANA");
        cv.put(COL_DESCRIPCION_INSUMO, "PIZZA CON SALSA, MUZARRELLA, RODAJAS DE TOMATES Y ACEITUNAS NEGRAS.");
        cv.put(COL_IMAGEN_INSUMO, "IMAGEN_INSUMO");
        cv.put(COL_PRECIO_INSUMO, 170);
        // cv.put(COL_STOCK_INSUMO, 10);
        cv.put(COL_ID_CATEGORIA_INSUMO, 6);
        db.insert(INSUMOS_MENUS_TABLE, COL_ID_INSUMO, cv);

        cv.put(COL_ID_INSUMO, 12);
        cv.put(COL_NOMBRE_INSUMO, "PIZZA RUCULA");
        cv.put(COL_DESCRIPCION_INSUMO, "PIZZA CON JAMON CRUDO, RUCULA, ACEITUNAS NEGRAS Y QUESO PARMESANO.");
        cv.put(COL_IMAGEN_INSUMO, "IMAGEN_INSUMO");
        cv.put(COL_PRECIO_INSUMO, 190);
        // cv.put(COL_STOCK_INSUMO, 10);
        cv.put(COL_ID_CATEGORIA_INSUMO, 6);
        db.insert(INSUMOS_MENUS_TABLE, COL_ID_INSUMO, cv);

        cv.put(COL_ID_INSUMO, 13);
        cv.put(COL_NOMBRE_INSUMO, "FLAN CASERO");
        cv.put(COL_DESCRIPCION_INSUMO, "FLAN DE HUEVO CON DULCE DE LECHE O CREMA.");
        cv.put(COL_IMAGEN_INSUMO, "IMAGEN_INSUMO");
        cv.put(COL_PRECIO_INSUMO, 45);
        // cv.put(COL_STOCK_INSUMO, 10);
        cv.put(COL_ID_CATEGORIA_INSUMO, 7);
        db.insert(INSUMOS_MENUS_TABLE, COL_ID_INSUMO, cv);

        cv.put(COL_ID_INSUMO, 14);
        cv.put(COL_NOMBRE_INSUMO, "HELADO");
        cv.put(COL_DESCRIPCION_INSUMO, "2 BOCHAS DE HELADO. ELECCION ENTRE CHOCOLATE, DULCE DE LECHE O CREMA.");
        cv.put(COL_IMAGEN_INSUMO, "IMAGEN_INSUMO");
        cv.put(COL_PRECIO_INSUMO, 38);
        // cv.put(COL_STOCK_INSUMO, 10);
        cv.put(COL_ID_CATEGORIA_INSUMO, 7);
        db.insert(INSUMOS_MENUS_TABLE, COL_ID_INSUMO, cv);

        cv.put(COL_ID_INSUMO, 15);
        cv.put(COL_NOMBRE_INSUMO, "DESAYUNO CHICO");
        cv.put(COL_DESCRIPCION_INSUMO, "CAFE O CAFE CON LECHE O TE CON 2 MEDIASLUNAS O CRIOLLOS.");
        cv.put(COL_IMAGEN_INSUMO, "IMAGEN_INSUMO");
        cv.put(COL_PRECIO_INSUMO, 62);
        // cv.put(COL_STOCK_INSUMO, 10);
        cv.put(COL_ID_CATEGORIA_INSUMO, 8);
        db.insert(INSUMOS_MENUS_TABLE, COL_ID_INSUMO, cv);

        cv.put(COL_ID_INSUMO, 16);
        cv.put(COL_NOMBRE_INSUMO, "DESAYUNO COMPLETO");
        cv.put(COL_DESCRIPCION_INSUMO, "CAFE O CAFE CON LECHE O TE CON 2 MEDIASLUNAS, PAN BLANCO O INTEGRAL CON DULCE Y QUESO UNTABLE. ADEMAS JUGO DE NARANJA.");
        cv.put(COL_IMAGEN_INSUMO, "IMAGEN_INSUMO");
        cv.put(COL_PRECIO_INSUMO, 95);
        // cv.put(COL_STOCK_INSUMO, 10);
        cv.put(COL_ID_CATEGORIA_INSUMO, 8);
        db.insert(INSUMOS_MENUS_TABLE, COL_ID_INSUMO, cv);

    }



    public Cursor obtenerCategorias() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT "
                + CATEGORIAS_TABLE + "." + COL_ID_CATEGORIA + " as _id, "
                + CATEGORIAS_TABLE + "." + COL_NOMBRE_CATEGORIA + " , "
                + CATEGORIAS_TABLE + "." + COL_IMAGEN_CATEGORIA
                + " from " + CATEGORIAS_TABLE , null);

        return cur;
    }


    public Cursor obtenerInsumosDeCategoria(int idCategoria) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT "
                + INSUMOS_MENUS_TABLE + "." + COL_ID_INSUMO + " as _id, "
                + INSUMOS_MENUS_TABLE + "." + COL_NOMBRE_INSUMO + ", "
                + INSUMOS_MENUS_TABLE + "." + COL_DESCRIPCION_INSUMO + " , "
                + INSUMOS_MENUS_TABLE + "." + COL_IMAGEN_INSUMO + " , "
                + INSUMOS_MENUS_TABLE + "." + COL_PRECIO_INSUMO + " , "
                + INSUMOS_MENUS_TABLE + "." + COL_STOCK_INSUMO + " , "
                + INSUMOS_MENUS_TABLE + "." + COL_ID_CATEGORIA_INSUMO
                +" from " + INSUMOS_MENUS_TABLE
                +" where "+ INSUMOS_MENUS_TABLE + "." + COL_ID_CATEGORIA_INSUMO + " = " + idCategoria, null);

        return cur;
    }

/*
    public void insertarMenuEnMesa(int idMesa, int idMenu, int cantidad)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(colNumeroMesa, idMesa);
        cv.put(colNumeroMenu, idMenu);
        cv.put(colCantidad, cantidad);
        // cv.put(colDept,2);
        db.insert(menuXMesaTable, colNumeroMesa, cv);
        db.close();
    }


    public boolean existeUsuario(String usuario, String contraseña)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT "
                + usersTable + "." + colID + " as _id, "
                + usersTable + "." + colUsername + ", "
                + usersTable + "." + colPassword
                +" from " + usersTable
                +" where "+ usersTable + "." + colUsername + " = '" + usuario + "' "
                +" AND "+ usersTable + "." + colPassword + " = '" + contraseña +"' ", null);
        int x = cur.getCount();
        if(x == 1)
            return true;
        else
            return false;

    }


*/
}
