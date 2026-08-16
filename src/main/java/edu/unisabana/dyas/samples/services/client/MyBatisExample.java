/*
 * Copyright (C) 2015 cesarvefe
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.unisabana.dyas.samples.services.client;


import java.sql.Date;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import edu.unisabana.dyas.sampleprj.dao.mybatis.mappers.ClienteMapper;
import edu.unisabana.dyas.sampleprj.dao.mybatis.mappers.ItemMapper;
import edu.unisabana.dyas.samples.entities.Item;
import edu.unisabana.dyas.samples.entities.TipoItem;


/**
 *
 * @author cesarvefe
 */
public class MyBatisExample {

    /**
     * Método que construye una fábrica de sesiones de MyBatis a partir del
     * archivo de configuración ubicado en src/main/resources
     *
     * @return instancia de SQLSessionFactory
     */
    public static SqlSessionFactory getSqlSessionFactory() {
        SqlSessionFactory sqlSessionFactory = null;
        if (sqlSessionFactory == null) {
            InputStream inputStream;
            try {
                inputStream = Resources.getResourceAsStream("mybatis-config.xml");
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e.getCause());
            }
        }
        return sqlSessionFactory;
    }

    /**
     * Programa principal de ejempo de uso de MyBATIS
     * @param args
     * @throws SQLException 
     */
    public static void main(String args[]) throws SQLException {
        SqlSessionFactory sessionfact = getSqlSessionFactory();

        SqlSession sqlss = sessionfact.openSession();

        
        try {
            ClienteMapper clienteMapper = sqlss.getMapper(ClienteMapper.class);
            ItemMapper itemMapper = sqlss.getMapper(ItemMapper.class);

            System.out.println("Clientes: " + clienteMapper.consultarClientes());
            System.out.println("Cliente 123456789: "
                    + clienteMapper.consultarCliente(123456789));

            Item itemDePrueba = new Item(
                    new TipoItem(3, "Herramienta"),
                    4,
                    "Taladro de prueba",
                    "Item usado para comprobar insertarItem",
                    Date.valueOf("2026-08-16"),
                    6500,
                    "Diario",
                    "Herramienta"
            );
            itemMapper.insertarItem(itemDePrueba);
            clienteMapper.agregarItemRentadoACliente(
                    555555555,
                    itemDePrueba.getId(),
                    Date.valueOf("2026-08-16"),
                    Date.valueOf("2026-08-20")
            );

            System.out.println("Item insertado: " + itemMapper.consultarItem(4));
            System.out.println("Todos los items: " + itemMapper.consultarItems());
            System.out.println("Cliente con alquiler de prueba: "
                    + clienteMapper.consultarCliente(555555555));
        } finally {
            // Las inserciones solo son de prueba: no se guardan al cerrar la sesión.
            sqlss.rollback();
            sqlss.close();
        }

        
        
    }


}
