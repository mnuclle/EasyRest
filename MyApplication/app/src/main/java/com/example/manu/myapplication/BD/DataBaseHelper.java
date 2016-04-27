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


    static final String INSUMOS_TABLE = "INSUMOS";
    static final String COL_ID_INSUMO = "ID_INSUMO";
    static final String COL_NOMBRE_INSUMO = "NOMBRE_INSUMO";
    static final String COL_DESCRIPCION_INSUMO = "DESCRIPCION_INSUMO";
    static final String COL_UNIDAD_DE_MEDIDA = "UNIDAD_DE_MEDIDA";
    static final String COL_STOCK_INSUMO = "STOCK_INSUMO";
    static final String COL_PRECIO_INSUMO = "PRECIO_INSUMO";
    static final String COL_IMAGEN_INSUMO = "IMAGEN_INSUMO";
    static final String COL_ID_CATEGORIA_INSUMO = "ID_CATEGORIA";



    static final String MENUS_TABLE = "MENUS";
    static final String COL_ID_MENU = "ID_MENU";
    static final String COL_NOMBRE_MENU = "NOMBRE_MENU";
    static final String COL_PRECIO_MENU = "PRECIO_MENU";
    static final String COL_ID_CATEGORIA_MENU = "ID_CATEGORIA";
    static final String COL_IMAGEN_MENU = "descripcionMenu";





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

        db.execSQL("CREATE TABLE " + INSUMOS_TABLE + " ("
                + COL_ID_INSUMO + " INTEGER PRIMARY KEY, "
                + COL_NOMBRE_INSUMO + " TEXT , "
                + COL_DESCRIPCION_INSUMO + " TEXT , "
                + COL_UNIDAD_DE_MEDIDA + " TEXT , "
                + COL_STOCK_INSUMO + " INTEGER , "
                + COL_PRECIO_INSUMO + " REAL , "
                + COL_IMAGEN_INSUMO + " TEXT , "
                + COL_ID_CATEGORIA_INSUMO + " INTEGER , "
                + " FOREIGN KEY (" + COL_ID_CATEGORIA_INSUMO
                + ") REFERENCES " + CATEGORIAS_TABLE + " (" + COL_ID_CATEGORIA + "));");

        db.execSQL("CREATE TABLE " + MENUS_TABLE + " ("
                + COL_ID_MENU + "  INTEGER PRIMARY KEY, "
                + COL_NOMBRE_MENU + " TEXT, "
                + COL_PRECIO_MENU + " REAL, "
                + COL_ID_CATEGORIA_MENU + " INTEGER , "
                + COL_IMAGEN_MENU + " TEXT , "
                + " FOREIGN KEY (" + COL_ID_CATEGORIA_MENU
                + ") REFERENCES " + CATEGORIAS_TABLE + " (" + COL_ID_CATEGORIA + "));");


/*
        db.execSQL("CREATE TRIGGER fk_menuXMesa_Menu " + " BEFORE INSERT "
                + " ON " + menuXMesaTable +
                " FOR EACH ROW BEGIN" + " SELECT CASE WHEN ((SELECT "
                + colIdMenu + " FROM " + menuTable + " WHERE " + colIdMenu
                + "=new." + colNumeroMenu + " ) IS NULL)"
                + " THEN RAISE (ABORT,'Foreign Key Violation') END;" + "  END;");
*/

        insertarUsuario(db);
        insertarMenusEntradas(db);
        insertarMenusPizzas(db);
        insertarMenusBebidas(db);
        insertarMenusPastas(db);
        insertarMenusTablas(db);
        insertarMenusPostres(db);
        insertarMenusCarnes(db);
        insertarMenusSandwiches(db);
        insertarMenuXMesa(db);



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MENUS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + INSUMOS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CATEGORIAS_TABLE);
       // db.execSQL("DROP TRIGGER IF EXISTS fk_menuXMesa_Menu");
        onCreate(db);

    }



    void insertarCategorias(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(COL_ID_CATEGORIA, "1");
        cv.put(COL_NOMBRE_CATEGORIA, "MINUTAS");
        cv.put(COL_IMAGEN_CATEGORIA, "MINUTAS");
        db.insert(CATEGORIAS_TABLE, COL_ID_CATEGORIA, cv);

        cv.put(COL_ID_CATEGORIA, "2");
        cv.put(COL_NOMBRE_CATEGORIA, "LOMITOS");
        cv.put(COL_IMAGEN_CATEGORIA, "LOMITOS");
        db.insert(CATEGORIAS_TABLE, COL_ID_CATEGORIA, cv);

        cv.put(COL_ID_CATEGORIA, "3");
        cv.put(COL_NOMBRE_CATEGORIA, "PASTAS");
        cv.put(COL_IMAGEN_CATEGORIA, "PASTAS");
        db.insert(CATEGORIAS_TABLE, COL_ID_CATEGORIA, cv);

        cv.put(COL_ID_CATEGORIA, "4");
        cv.put(COL_NOMBRE_CATEGORIA, "TABLAS");
        cv.put(COL_IMAGEN_CATEGORIA, "TABLAS");
        db.insert(CATEGORIAS_TABLE, COL_ID_CATEGORIA, cv);

        cv.put(COL_ID_CATEGORIA, "5");
        cv.put(COL_NOMBRE_CATEGORIA, "BEBIDAS");
        cv.put(COL_IMAGEN_CATEGORIA, "BEBIDAS");
        db.insert(CATEGORIAS_TABLE, COL_ID_CATEGORIA, cv);

        cv.put(COL_ID_CATEGORIA, "6");
        cv.put(COL_NOMBRE_CATEGORIA, "PIZZAS");
        cv.put(COL_IMAGEN_CATEGORIA, "PIZZAS");
        db.insert(CATEGORIAS_TABLE, COL_ID_CATEGORIA, cv);

        cv.put(COL_ID_CATEGORIA, "7");
        cv.put(COL_NOMBRE_CATEGORIA, "POSTRES");
        cv.put(COL_IMAGEN_CATEGORIA, "POSTRES");
        db.insert(CATEGORIAS_TABLE, COL_ID_CATEGORIA, cv);

        cv.put(COL_ID_CATEGORIA, "8");
        cv.put(COL_NOMBRE_CATEGORIA, "DESAYUNO");
        cv.put(COL_IMAGEN_CATEGORIA, "DESAYUNO");
        db.insert(CATEGORIAS_TABLE, COL_ID_CATEGORIA, cv);

    }


    void insertarMenuXMesa(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(colNumeroMesa, 1);
        cv.put(colNumeroMenu, 1);
        cv.put(colCantidad, 5);
        db.insert(menuXMesaTable, colNumeroMesa, cv);

        cv.put(colNumeroMesa, 1);
        cv.put(colNumeroMenu, 2);
        cv.put(colCantidad, 2);
        db.insert(menuXMesaTable, colNumeroMesa, cv);

        cv.put(colNumeroMesa, 1);
        cv.put(colNumeroMenu, 3);
        cv.put(colCantidad, 3);
        db.insert(menuXMesaTable, colNumeroMesa, cv);

        cv.put(colNumeroMesa, 1);
        cv.put(colNumeroMenu, 4);
        cv.put(colCantidad, 4);
        db.insert(menuXMesaTable, colNumeroMesa, cv);

        cv.put(colNumeroMesa, 1);
        cv.put(colNumeroMenu, 5);
        cv.put(colCantidad, 1);
        db.insert(menuXMesaTable, colNumeroMesa, cv);

        cv.put(colNumeroMesa, 1);
        cv.put(colNumeroMenu, 6);
        cv.put(colCantidad, 5);
        db.insert(menuXMesaTable, colNumeroMesa, cv);

        cv.put(colNumeroMesa, 1);
        cv.put(colNumeroMenu, 7);
        cv.put(colCantidad, 2);
        db.insert(menuXMesaTable, colNumeroMesa, cv);

        cv.put(colNumeroMesa, 1);
        cv.put(colNumeroMenu, 8);
        cv.put(colCantidad, 3);
        db.insert(menuXMesaTable, colNumeroMesa, cv);

        cv.put(colNumeroMesa, 1);
        cv.put(colNumeroMenu, 9);
        cv.put(colCantidad, 4);
        db.insert(menuXMesaTable, colNumeroMesa, cv);

        cv.put(colNumeroMesa, 1);
        cv.put(colNumeroMenu, 10);
        cv.put(colCantidad, 1);
        db.insert(menuXMesaTable, colNumeroMesa, cv);
    }



    void insertarMenusEntradas(SQLiteDatabase db) {

        ContentValues cv = new ContentValues();

        cv.put(colNombreMenu, "EMPANADA ARABE");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Entrada_Empanada_Arabe_HD_BAJA_zps4s9ubuxf.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Entrada_Empanada_Arabe_HD_ALTA_zpsdbustep1.jpg");
        cv.put(colDescripcionMenu, "Empanada arabe con masa casera.");
        cv.put(colPrecioMenu, 25);
        cv.put(colIdCategoriaMenu, 1);
        db.insert(menuTable, colIdMenu, cv);

        cv.put(colNombreMenu, "EMPANADA CRIOLLA FRITA");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Entrada_Empanada_Criolla_Frita_HD_BAJA_zpsv5hcohzm.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Entrada_Empanada_Criolla_Frita_HD_ALTA_zpsr8x9uafr.jpg");
        cv.put(colDescripcionMenu, "Empanada rellena con carne, cebolla, huevo y condimentadas. Fritas.");
        cv.put(colPrecioMenu, 23);
        cv.put(colIdCategoriaMenu, 1);
        db.insert(menuTable, colIdMenu, cv);

        cv.put(colNombreMenu, "EMPANADA CRIOLLA AL HORNO");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Entrada_Empanada_Criolla_Horno_HD_BAJA_zpsyzxnwebz.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Entrada_Empanada_Criolla_Horno_HD_ALTA_zpshxouwiy5.jpg");
        cv.put(colDescripcionMenu, "Empanada rellena con carne, cebolla, huevo y condimentadas. Al Horno.");
        cv.put(colPrecioMenu, 23);
        cv.put(colIdCategoriaMenu, 1);
        db.insert(menuTable, colIdMenu, cv);

        cv.put(colNombreMenu, "EMPANADA JAMON Y QUESO AL HORNO");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Entrada_Empanada_Jamon_Queso_HD_BAJA_zps9cngf7zo.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Entrada_Empanada_Jamon_Queso_HD_ALTA_zpsgmxgcpjg.jpg");
        cv.put(colDescripcionMenu, "Empanada de Jamon del Campo y Queso del Campo.");
        cv.put(colPrecioMenu, 23);
        cv.put(colIdCategoriaMenu, 1);
        db.insert(menuTable, colIdMenu, cv);

        cv.put(colNombreMenu, "NACHOS CON SALSA CRIOLLA");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Entrada_Nachos_HD_BAJA_zpsmnwqgrsq.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Entrada_Nachos_HD_ALTA_zpsmvbuawd8.jpg");
        cv.put(colDescripcionMenu, "Nachos con salsa de pimiento rojo, verde y cebolla");
        cv.put(colPrecioMenu, 80);
        cv.put(colIdCategoriaMenu, 1);
        db.insert(menuTable, colIdMenu, cv);

        cv.put(colNombreMenu, "RABAS");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Entrada_Rabas_HD_BAJA_zpslctxxodx.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Entrada_Rabas_HD_ALTA_zpsks3eciax.jpg");
        cv.put(colDescripcionMenu, "Porción de Rabas Fritas.");
        cv.put(colPrecioMenu, 80);
        cv.put(colIdCategoriaMenu, 1);
        db.insert(menuTable, colIdMenu, cv);


    }


    void insertarMenusTablas(SQLiteDatabase db) {

        ContentValues cv = new ContentValues();

        cv.put(colNombreMenu, "TABLA FIAMBRES PARA 2");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Tablas_Fiabres_2P_HD_BAJA_zps5drfnk5p.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Tablas_Fiabres_2P_HD_ALTA_zpsokb2velv.jpg");
        cv.put(colDescripcionMenu, "Tabla con jamon, queso de campo, queso roquefort, salame, bondiola, jamon crudo y aceitunas.");
        cv.put(colPrecioMenu, 180);
        cv.put(colIdCategoriaMenu, 2);
        db.insert(menuTable, colIdMenu, cv);

        cv.put(colNombreMenu, "TABLA FIAMBRES PARA 4");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Tablas_Fiabres_4P_HD_BAJA_zpsvc2q2w9a.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Tablas_Fiabres_4P_HD_ALTA_zpsqs9wb5co.jpg");
        cv.put(colDescripcionMenu, "Tabla con jamon, queso de campo, queso roquefort, salame, bondiola, jamon crudo y aceitunas.");
        cv.put(colPrecioMenu, 280);
        cv.put(colIdCategoriaMenu, 2);
        db.insert(menuTable, colIdMenu, cv);

        cv.put(colNombreMenu, "TABLA CORDOBEZA PARA 2");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Tablas_Fritas_Carnes_HD_BAJA_zpsjq0xazel.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Tablas_Fritas_Carnes_HD_ALTA_zpspse38dcs.jpg");
        cv.put(colDescripcionMenu, "Tabla con papas fritas, y carne picada con salsa criolla y salsa chedar.");
        cv.put(colPrecioMenu, 210);
        cv.put(colIdCategoriaMenu, 2);
        db.insert(menuTable, colIdMenu, cv);

        cv.put(colNombreMenu, "TABLA RUSTICA PARA 6 PERSONAS");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Tablas_Rustica_HD_BAJA_zps0lssszwj.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Tablas_Rustica_HD_ALTA_zpsnoxdfhlv.jpg");
        cv.put(colDescripcionMenu, "Tabla con huevo duro, jamon crudo, queso roquefort, queso chedar, queso dambo, pollo frito, chorizos, papas fritas, bondiola, pizza y hamburguesas fritas con mayonesa casera.");
        cv.put(colPrecioMenu, 450);
        cv.put(colIdCategoriaMenu, 2);
        db.insert(menuTable, colIdMenu, cv);

    }


    void insertarMenusSandwiches(SQLiteDatabase db) {

        ContentValues cv = new ContentValues();

        cv.put(colNombreMenu, "HAMBURGUESA COMPLETA");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Sandwich_Hamburguesa_Completa_HD_BAJA_zpsxcwdfvgm.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Sandwich_Hamburguesa_Completa_HD_ALTA_zpstzv1xlkp.jpg");
        cv.put(colDescripcionMenu, "Sandwich de hamburguesa con queso, tomate, lechuga, huevo y carne. Con papas fritas.");
        cv.put(colPrecioMenu, 95);
        cv.put(colIdCategoriaMenu, 3);
        db.insert(menuTable, colIdMenu, cv);

        cv.put(colNombreMenu, "LOMITO COMPLETO");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Sandwich_Lomito_Completo_HD_BAJA_zpsd3zgmlej.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Sandwich_Lomito_Completo_HD_ALTA_zpsdhmsu4cf.jpg");
        cv.put(colDescripcionMenu, "Lomito con queso, tomate, lechuga, huevo y carne de lomo. Con papas fritas.");
        cv.put(colPrecioMenu, 115);
        cv.put(colIdCategoriaMenu, 3);
        db.insert(menuTable, colIdMenu, cv);

        cv.put(colNombreMenu, "SANDWICH DE MILANESA");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Sandwich_Milanesa_HD_BAJA_zps4lb3sshi.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Sandwich_Milanesa_HD_ALTA_zpsvg0q2ro6.jpg");
        cv.put(colDescripcionMenu, "Sandwich con milanesa de carne o pollo, pan de lomo, tomate, lechuga y huevo.");
        cv.put(colPrecioMenu, 105);
        cv.put(colIdCategoriaMenu, 3);
        db.insert(menuTable, colIdMenu, cv);

        cv.put(colNombreMenu, "SANDWICH DE PAN FRANCES");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Sandwich_Pan_Frances_HD_BAJA_zpsigpq3cfo.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Sandwich_Pan_Frances_HD_ALTA_zpsjm2l56ix.jpg");
        cv.put(colDescripcionMenu, "Sandwich con jamon, queso, tomate y lechuga.");
        cv.put(colPrecioMenu, 85);
        cv.put(colIdCategoriaMenu, 3);
        db.insert(menuTable, colIdMenu, cv);

        cv.put(colNombreMenu, "SANDWICH DE PAN DE SALVADO");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Sandwich_Pan_Salvado_HD_BAJA_zps4sw3nrcs.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Sandwich_Pan_Salvado_HD_ALTA_zpsad9cofrj.jpg");
        cv.put(colDescripcionMenu, "Sandwich con jamon, queso, tomate y lechuga.");
        cv.put(colPrecioMenu, 90);
        cv.put(colIdCategoriaMenu, 3);
        db.insert(menuTable, colIdMenu, cv);

        cv.put(colNombreMenu, "TOSTADO COMPLETO");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Sandwich_Tostado_Completo_HD_BAJA_zpstqtppkro.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Sandwich_Tostado_Completo_HD_ALTA_zpsgvwpki70.jpg");
        cv.put(colDescripcionMenu, "2 Sandwich de miga de jamon, queso, lechuga y tomate tostados.");
        cv.put(colPrecioMenu, 70);
        cv.put(colIdCategoriaMenu, 3);
        db.insert(menuTable, colIdMenu, cv);


    }

    void insertarMenusCarnes(SQLiteDatabase db) {

        ContentValues cv = new ContentValues();

        cv.put(colNombreMenu, "BIFE DE CHORIZO");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Carnes_Bife_Chorizo_HD_BAJA_zpshhidhj9c.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Carnes_Bife_Chorizo_HD_ALTA_zpsnme9wgk4.jpg");
        cv.put(colDescripcionMenu, "Bife de chorizo a la parrilla.");
        cv.put(colPrecioMenu, 100);
        cv.put(colIdCategoriaMenu, 4);
        db.insert(menuTable, colIdMenu, cv);

        cv.put(colNombreMenu, "CABRITO AL HORNO");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Carnes_Cabrito_Horno_HD_BAJA_zps46jdhvf8.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Carnes_Cabrito_Horno_HD_ALTA_zps33gnudbh.jpg");
        cv.put(colDescripcionMenu, "Carne de cabrito al horno con reduccion de vino malbec.");
        cv.put(colPrecioMenu, 150);
        cv.put(colIdCategoriaMenu, 4);
        db.insert(menuTable, colIdMenu, cv);

        cv.put(colNombreMenu, "CORDERO ASADO");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Carnes_Cordero_Asado_HD_BAJA_zpsov7aelyo.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Carnes_Cordero_Asado_HD_ALTA_zps2c6ei8if.jpg");
        cv.put(colDescripcionMenu, "Carne de cordero asada a la llama.");
        cv.put(colPrecioMenu, 150);
        cv.put(colIdCategoriaMenu, 4);
        db.insert(menuTable, colIdMenu, cv);

        cv.put(colNombreMenu, "BIFES DE CUADRIL");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Carnes_Cuadril_Horno_HD_BAJA_zpswz1xfzid.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Carnes_Cuadril_Horno_HD_ALTA_zpsvadgub6n.jpg");
        cv.put(colDescripcionMenu, "3 bifes de cuadril a la plancha con un toque cebolla de verdeo");
        cv.put(colPrecioMenu, 100);
        cv.put(colIdCategoriaMenu, 4);
        db.insert(menuTable, colIdMenu, cv);

        cv.put(colNombreMenu, "DORADO AL HORNO CON PAPAS Y MANZANA CARAMELIZADA");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Carnes_Dorado_Horno_HD_BAJA_zps0vz8mced.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Carnes_Dorado_Horno_HD_ALTA_zpsleqdcjlx.jpg");
        cv.put(colDescripcionMenu, "Pescado Dorado al horno acompañado de papas y manzanas cortadas a la juliana caramelizadas.");
        cv.put(colPrecioMenu, 145);
        cv.put(colIdCategoriaMenu, 4);
        db.insert(menuTable, colIdMenu, cv);

        cv.put(colNombreMenu, "MATAMBRE DE VACA A LA PARRILLA");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Carnes_Matambre_HD_BAJA_zpsfiaqitfh.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Carnes_Matambre_HD_ALTA_zpsrodu2nqw.jpg");
        cv.put(colDescripcionMenu, "Porción de matambre de vaca cocinado en parrilla.");
        cv.put(colPrecioMenu, 125);
        cv.put(colIdCategoriaMenu, 4);
        db.insert(menuTable, colIdMenu, cv);

        cv.put(colNombreMenu, "MILANESA DE CARNE A CABALLO CON PAPAS FRITAS");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Carnes_Milanesa_ACaballo_HD_ALTA_zpsfvjlkjdg.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Carnes_Milanesa_ACaballo_HD_ALTA_zpsfvjlkjdg.jpg");
        cv.put(colDescripcionMenu, "Una milanesa frita de carne de vaca, con 2 huevos fritos encima de la misma, y deliciosas papas fritas.");
        cv.put(colPrecioMenu, 95);
        cv.put(colIdCategoriaMenu, 4);
        db.insert(menuTable, colIdMenu, cv);

        cv.put(colNombreMenu, "MILANESA DE CARNE A LA NAPOLITANA CON PAPAS AL HORNO");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Carnes_Milanesa_Carne_HD_BAJA_zpsfldebzev.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Carnes_Milanesa_Carne_HD_ALTA_zpsceownubu.jpg");
        cv.put(colDescripcionMenu, "Una milanesa frita de carne de vaca a la napolitana, con papas rústicas.");
        cv.put(colPrecioMenu, 85);
        cv.put(colIdCategoriaMenu, 4);
        db.insert(menuTable, colIdMenu, cv);

        cv.put(colNombreMenu, "MILANESA DE POLLO CON PAPAS FRITAS");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Carnes_Milanesa_Pollo_HD_BAJA_zpsb7cv3xsk.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Carnes_Milanesa_Pollo_HD_ALTA_zpsbqicdjrw.jpg");
        cv.put(colDescripcionMenu, "Una milanesa de pollo frita con papas fritas.");
        cv.put(colPrecioMenu, 85);
        cv.put(colIdCategoriaMenu, 4);
        db.insert(menuTable, colIdMenu, cv);

        cv.put(colNombreMenu, "PATA MUSLO AL HORNO");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Carnes_PataMuslo_Horno_HD_BAJA_zpsm2tdoask.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Carnes_PataMuslo_Horno_HD_ALTA_zpsh4vtyhd1.jpg");
        cv.put(colDescripcionMenu, "Pata muslo al horno cocinado con salsa de pimentón.");
        cv.put(colPrecioMenu, 95);
        cv.put(colIdCategoriaMenu, 4);
        db.insert(menuTable, colIdMenu, cv);

        cv.put(colNombreMenu, "PECHUGA DE POLLO AL HORNO");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Carnes_Pechuga_Horno_HD_BAJA_zpsndqfmnfn.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Carnes_Pechuga_Horno_HD_ALTA_zpscfv3vabo.jpg");
        cv.put(colDescripcionMenu, "Pechuga de pollo al limón cocinada al horno.");
        cv.put(colPrecioMenu, 105);
        cv.put(colIdCategoriaMenu, 4);
        db.insert(menuTable, colIdMenu, cv);

        cv.put(colNombreMenu, "PECHUGA DE POLLO A LA SUIZA");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Carnes_Pechuga_Suiza_HD_BAJA_zps4llbku7p.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Carnes_Pechuga_Suiza_HD_ALTA_zpsfptldok3.jpg");
        cv.put(colDescripcionMenu, "Pechuga de pollo cocinada a la plancha con salsa suiza.");
        cv.put(colPrecioMenu, 110);
        cv.put(colIdCategoriaMenu, 4);
        db.insert(menuTable, colIdMenu, cv);

        cv.put(colNombreMenu, "POLLO DE MAR AL HORNO");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Carnes_Pollo_De_Mar_Horno_HD_BAJA_zpss8mnvmx7.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Carnes_Pollo_De_Mar_Horno_HD_ALTA_zpshtwrohuw.jpg");
        cv.put(colDescripcionMenu, "Pescado Pollo de Mar al horno con salsa de tomate.");
        cv.put(colPrecioMenu, 165);
        cv.put(colIdCategoriaMenu, 4);
        db.insert(menuTable, colIdMenu, cv);

        cv.put(colNombreMenu, "VACIO RELLENO ACOMPAÑADO DE CROQUETAS DE FIDEO.");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Carnes_Vacio_HD_BAJA_zpshnoz8yay.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Carnes_Vacio_HD_ALTA_zpsp3agcmxs.jpg");
        cv.put(colDescripcionMenu, "Porcio de Vacio de vaca al horno rellena con pimiento y cebolla acompañado de croquetas de fideo.");
        cv.put(colPrecioMenu, 155);
        cv.put(colIdCategoriaMenu, 4);
        db.insert(menuTable, colIdMenu, cv);

    }

    void insertarMenusPizzas(SQLiteDatabase db) {

        ContentValues cv = new ContentValues();

        cv.put(colNombreMenu, "PIZZA DE ANCHOAS");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Pizza_Anchoas_HD_BAJA_zps8qsw0ev2.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Pizza_Anchoas_HD_ALTA_zpsfi7afx9j.jpg");
        cv.put(colDescripcionMenu, "Pizza casera con salsa, mozzarella, anchoas y aceitunas negras.");
        cv.put(colPrecioMenu, 125);
        cv.put(colIdCategoriaMenu, 5);
        db.insert(menuTable, colIdMenu, cv);

        cv.put(colNombreMenu, "PIZZA CALABRESA");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Pizza_Calabresa_HD_BAJA_zpsaztusobz.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Pizza_Calabresa_HD_ALTA_zpsu2vzvjid.jpg");
        cv.put(colDescripcionMenu, "Pizza casera con salsa, mozzarella y salame picado grueso.");
        cv.put(colPrecioMenu, 120);
        cv.put(colIdCategoriaMenu, 5);
        db.insert(menuTable, colIdMenu, cv);

        cv.put(colNombreMenu, "PIZZA ESPECIAL CON MORRON");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Pizza_Especial_Huevo_HD_BAJA_zpstlpziqvl.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Pizza_Especial_Huevo_HD_ALTA_zpsxg5n6mgy.jpg");
        cv.put(colDescripcionMenu, "Pizza casera con salsa, aceitunas verdes, mozzarella, jamón y huevo duro.");
        cv.put(colPrecioMenu, 105);
        cv.put(colIdCategoriaMenu, 5);
        db.insert(menuTable, colIdMenu, cv);

        cv.put(colNombreMenu, "PIZZA ESPECIAL CON MORRON Y HUEVO");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Pizza_Especial_Jamon_HD_BAJA_zpsnckunkdq.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Pizza_Especial_Jamon_HD_ALTA_zps6neqelp4.jpg");
        cv.put(colDescripcionMenu, "Pizza casera con salsa, mozzarella, jamon, huevo, morron y aceitunas verdes.");
        cv.put(colPrecioMenu, 115);
        cv.put(colIdCategoriaMenu, 5);
        db.insert(menuTable, colIdMenu, cv);

        cv.put(colNombreMenu, "PIZZA RUCULA");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Pizza_Rucula_HD_BAJA_zpssptk5dka.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Pizza_Rucula_HD_ALTA_zpsk0lo9ahd.jpg");
        cv.put(colDescripcionMenu, "Pizza casera con salsa, mozzarella, rucula, queso parmesano y aceitunas negras.");
        cv.put(colPrecioMenu, 135);
        cv.put(colIdCategoriaMenu, 5);
        db.insert(menuTable, colIdMenu, cv);

        cv.put(colNombreMenu, "PIZZA FUGAZZA");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Pizza_Fugazza_HD_BAJA_zpswany2qij.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Pizza_Fugazza_HD_ALTA_zpsksrjxyoo.jpg");
        cv.put(colDescripcionMenu, "Pizza casera mozzarella y cebolla rehogada.");
        cv.put(colPrecioMenu, 125);
        cv.put(colIdCategoriaMenu, 5);
        db.insert(menuTable, colIdMenu, cv);

        cv.put(colNombreMenu, "PIZZA DE PALMITOS");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Pizza_Palmitos_HD_BAJA_zpstiimukkb.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Pizza_Palmitos_HD_ALTA_zpsneo6nopi.jpg");
        cv.put(colDescripcionMenu, "Pizza casera con salsa, mozzarella, jamon, morrones, palmitos, salsa golf y aceitunas verdes.");
        cv.put(colPrecioMenu, 155);
        cv.put(colIdCategoriaMenu, 5);
        db.insert(menuTable, colIdMenu, cv);

        cv.put(colNombreMenu, "PIZZA DE ROQUEFORT");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Pizza_Roquefort_HD_BAJA_zps1ovzegm7.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Pizza_Roquefort_HD_ALTA_zpsnxqdvbdj.jpg");
        cv.put(colDescripcionMenu, "Pizza casera con salsa, mozzarella y roquefort.");
        cv.put(colPrecioMenu, 135);
        cv.put(colIdCategoriaMenu, 5);
        db.insert(menuTable, colIdMenu, cv);




    }


    void insertarMenusPastas(SQLiteDatabase db) {

        ContentValues cv = new ContentValues();

        cv.put(colNombreMenu, "CANELONES CON ACELGA Y CARNE");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Pastas_Canelones_HD_BAJA_zpsnbz1gdwv.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Pastas_Canelones_HD_aLTA_zpsh7lfrc12.jpg");
        cv.put(colDescripcionMenu, "Canelones rellenos con acelga y carne, con salsa roja.");
        cv.put(colPrecioMenu, 95);
        cv.put(colIdCategoriaMenu, 6);
        db.insert(menuTable, colIdMenu, cv);

        cv.put(colNombreMenu, "FIDEOS AL PESTO");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Pastas_Fideos_HD_BAJA_zpsdbtlgz1s.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Pastas_Fideos_HD_ALTA_zpsbjzbdshx.jpg");
        cv.put(colDescripcionMenu, "Fideos tallarines al pesto.");
        cv.put(colPrecioMenu, 80);
        cv.put(colIdCategoriaMenu, 6);
        db.insert(menuTable, colIdMenu, cv);

        cv.put(colNombreMenu, "LASAGNA");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Pastas_Lasagna_hd_BAJA_zpsdtg3qnjr.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Pastas_Lasagna_hd_ALTA_zps5pw6hwr9.jpg");
        cv.put(colDescripcionMenu, "Porcion de lasagna con salsa roja y salsa blanca.");
        cv.put(colPrecioMenu, 95);
        cv.put(colIdCategoriaMenu, 6);
        db.insert(menuTable, colIdMenu, cv);

        cv.put(colNombreMenu, "ÑOQUIS CON SALSA ROJA");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Pastas_Noquis_HD_BAJA_zps8seioe8u.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Pastas_Noquis_HD_ALTA_zpsckfy3pt8.jpg");
        cv.put(colDescripcionMenu, "Ñoquis caseros, con salsa roja.");
        cv.put(colPrecioMenu, 95);
        cv.put(colIdCategoriaMenu, 6);
        db.insert(menuTable, colIdMenu, cv);

        cv.put(colNombreMenu, "RAVIOLES CON SALSA ROJA");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Pastas_Ravioles_HD_BAJA_zpskn46gfwx.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Pastas_Ravioles_HD_ALTA_zpsjtwy7dt1.jpg");
        cv.put(colDescripcionMenu, "Ravioles de 4 quesos con salsa roja.");
        cv.put(colPrecioMenu, 95);
        cv.put(colIdCategoriaMenu, 6);
        db.insert(menuTable, colIdMenu, cv);

        cv.put(colNombreMenu, "SORRENTINOS CON SALSA Y CREMA");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Pastas_Sorrentinos_HD_BAJA_zpszx8iqvqs.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Pastas_Sorrentinos_HD_ALTA_zpsuq990obw.jpg");
        cv.put(colDescripcionMenu, "SORRENTINOS CON SALSA ROJA Y CREMA.");
        cv.put(colPrecioMenu, 95);
        cv.put(colIdCategoriaMenu, 6);
        db.insert(menuTable, colIdMenu, cv);



    }


    void insertarMenusBebidas(SQLiteDatabase db) {

        ContentValues cv = new ContentValues();
        cv.put(colNombreMenu, "Jarra de Cerveza");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Bebida_Cerveza_Jarra_HD_BAJA_zpszwfg1kqq.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Bebida_Cerveza_Jarra_HD_ALTA_zps3xcgoxgg.jpg");
        cv.put(colDescripcionMenu, "Jarra de litro de Cerveza Artesanal Rubia");
        cv.put(colPrecioMenu, 80);
        cv.put(colIdCategoriaMenu, 7);
        db.insert(menuTable, colIdMenu, cv);

        cv.put(colNombreMenu, "Vaso de Cerveza");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Bebida_Cerveza_Vaso_HD_BAJA_zpsqenleeiz.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Bebida_Cerveza_Vaso_HD_ALTA_zpsbplfpddd.jpg");
        cv.put(colDescripcionMenu, "Vaso de Cerveza Artesanal, de diferentes gustos.");
        cv.put(colPrecioMenu, 50);
        cv.put(colIdCategoriaMenu, 7);
        db.insert(menuTable, colIdMenu, cv);

        cv.put(colNombreMenu, "COCA COLA 1,5L");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Bebida_Coca_15L_BAJA_zps3rkqizy9.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Bebida_Coca_15L_ALTA_zpsjfulrhwl.jpg");
        cv.put(colDescripcionMenu, "Bebida sabor cola, marca Coca de 1,5L.");
        cv.put(colPrecioMenu, 80);
        cv.put(colIdCategoriaMenu, 7);
        db.insert(menuTable, colIdMenu, cv);

        cv.put(colNombreMenu, "COCA COLA ZERO 1,5L");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Bebida_Coca_15L_Zero_BAJA_zpsnl68njii.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Bebida_Coca_15L_Zero_ALTA_zps7ouexza0.jpg");
        cv.put(colDescripcionMenu, "Bebida sabor cola, marca Coca de 1,5L sin azucar.");
        cv.put(colPrecioMenu, 85);
        cv.put(colIdCategoriaMenu, 7);
        db.insert(menuTable, colIdMenu, cv);

        cv.put(colNombreMenu, "COCA COLA 375ml.");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Bebida_Coca_Chica_HD_BAJA_zps8sgsimpz.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Bebida_Coca_Chica_HD_ALTA_zps9gpde3ml.jpg");
        cv.put(colDescripcionMenu, "Bebida sabor cola, marca Coca de 375ml.");
        cv.put(colPrecioMenu, 35);
        cv.put(colIdCategoriaMenu, 7);
        db.insert(menuTable, colIdMenu, cv);

        cv.put(colNombreMenu, "FANTA 1,5L");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Bebida_Fanta_15L_HD_BAJA_zpsqvjlauxs.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Bebida_Fanta_15L_HD_ALTA_zpsyjycoerj.jpg");
        cv.put(colDescripcionMenu, "Bebida sabor naranja, marca Coca de 1,5L.");
        cv.put(colPrecioMenu, 80);
        cv.put(colIdCategoriaMenu, 7);
        db.insert(menuTable, colIdMenu, cv);

        cv.put(colNombreMenu, "SPRITE 1,5L");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Bebida_Sprite_15l_HD_BAJA_zpsngb0gbwo.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Bebida_Sprite_15l_HD_ALTA_zpsyfjqt3rq.jpg");
        cv.put(colDescripcionMenu, "Bebida sabor lima, marca Coca de 1,5L.");
        cv.put(colPrecioMenu, 80);
        cv.put(colIdCategoriaMenu, 7);
        db.insert(menuTable, colIdMenu, cv);


    }


    void insertarMenusPostres(SQLiteDatabase db) {

        ContentValues cv = new ContentValues();
        cv.put(colNombreMenu, "BUDIN DE PAN");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Postras_Budin_Pan_HD_BAJA_zpsh14aatgy.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Postras_Budin_Pan_HD_ALTA_zpsznvbmzpm.jpg");
        cv.put(colDescripcionMenu, "Budin de pan casero con caramelo.");
        cv.put(colPrecioMenu, 45);
        cv.put(colIdCategoriaMenu, 8);
        db.insert(menuTable, colIdMenu, cv);

        cv.put(colNombreMenu, "TIRAMISU");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Postras_Tiramisu_BAJA_zpscwkqrx6e.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Postras_Tiramisu_ALTA_zpshrxmqo5s.jpg");
        cv.put(colDescripcionMenu, "Postre con queso crema, cafe, bizcochuelo y cacao.");
        cv.put(colPrecioMenu, 55);
        cv.put(colIdCategoriaMenu, 8);
        db.insert(menuTable, colIdMenu, cv);

        cv.put(colNombreMenu, "3 BOCHAS DE HELADO");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Postres_Bochas_Helado_HD_BAJA_zpsujiy3mia.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Postres_Bochas_Helado_HD_ALTA_zpsgiidyjwa.jpg");
        cv.put(colDescripcionMenu, "Helado en bochas de dulce de leche, chocolate, frutilla.");
        cv.put(colPrecioMenu, 50);
        cv.put(colIdCategoriaMenu, 8);
        db.insert(menuTable, colIdMenu, cv);

        cv.put(colNombreMenu, "BOMBON ESCOCES");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Postres_Bombo_Escoses_HD_BAJA_zpswpexywqp.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Postres_Bombo_Escoses_HD_ALTA_zpsmegd1sdx.jpg");
        cv.put(colDescripcionMenu, "Helado cubierto de chocolate con centro de dulce de leche.");
        cv.put(colPrecioMenu, 40);
        cv.put(colIdCategoriaMenu, 8);
        db.insert(menuTable, colIdMenu, cv);

        cv.put(colNombreMenu, "ENSALADA DE FRUTAS");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Postres_Ensalada_Fruta_HD_BAJA_zpsjjdy4pny.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Postres_Ensalada_Fruta_HD_ALTA_zpslr8klnak.jpg");
        cv.put(colDescripcionMenu, "Ensalada de frutas con frutilla, naranja, banana, uvas, durazno, kiwi y manzana.");
        cv.put(colPrecioMenu, 55);
        cv.put(colIdCategoriaMenu, 8);
        db.insert(menuTable, colIdMenu, cv);

        cv.put(colNombreMenu, "FLAN CASERO");
        cv.put(colUrlFotoLowQMenu,"http://i1379.photobucket.com/albums/ah133/daniel0613/Postres_Flan_HD_BAJA_zpsntkhcyoo.jpg");
        cv.put(colUrlFotoHighQMenu, "http://i1379.photobucket.com/albums/ah133/daniel0613/Postres_Flan_HD_ALTA_zpsg80zivm2.jpg");
        cv.put(colDescripcionMenu, "Flan casero con caramelo.");
        cv.put(colPrecioMenu, 45);
        cv.put(colIdCategoriaMenu, 8);
        db.insert(menuTable, colIdMenu, cv);



    }









    public Cursor obtenerMenusConIdMesa(int idMesa) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT "
                + menuTable + "." + colIdMenu + " as _id, "
                + menuTable +"."+ colNombreMenu + ", "
                + menuXMesaTable +"." + colCantidad + " , "
                + menuTable +"."+colPrecioMenu
                +" from " + menuTable +" , " + menuXMesaTable
                +" where "+ menuTable + "." + colIdMenu + " = "
                + menuXMesaTable +"."+colNumeroMenu
                + " AND " + menuXMesaTable + "." + colNumeroMesa + " = " + idMesa, null);

        return cur;
    }

    public Cursor obtenerMenusConIdCategoria(int idCategoria) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT "
                + menuTable + "." + colIdMenu + " as _id, "
                + menuTable + "." + colNombreMenu + ", "
                + menuTable + "." + colDescripcionMenu + " , "
                + menuTable + "." + colUrlFotoLowQMenu + " , "
                + menuTable + "." + colUrlFotoHighQMenu + " , "
                + menuTable + "." + colPrecioMenu + " , "
                + menuTable + "." + colIdCategoriaMenu
                +" from " + menuTable
                +" where "+ menuTable + "." + colIdCategoriaMenu + " = " + idCategoria, null);

        return cur;
    }


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



}
