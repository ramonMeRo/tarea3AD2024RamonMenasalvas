<?xml version="1.0" encoding="UTF-8"?>
<helpset version="1.0">
    <title>Sistema de Ayuda de la Aplicación</title>
    <maps>
        <homeID>principal</homeID>
        <mapref location="mapa.jhm"/>
    </maps>
    
    <view>
        <name>Tabla Contenidos</name>
        <label>Tabla de contenidos</label>
        <type>javax.help.TOCView</type>
        <data>tablacontenidos.xml</data>
    </view>

    <view>
        <name>Indice</name>
        <label>El índice</label>
        <type>javax.help.IndexView</type>
        <data>indice.xml</data>
    </view>

    <view>
        <name>Buscar</name>
        <label>Buscar</label>
        <type>javax.help.SearchView</type>
        <data engine="com.sun.java.help.search.DefaultSearchEngine">
            JavaHelpSearch
        </data>
    </view>
</helpset>
