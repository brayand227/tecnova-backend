package com.tienda.backend.config;

import com.tienda.backend.model.Categoria;
import com.tienda.backend.model.Producto;
import com.tienda.backend.model.Rol;
import com.tienda.backend.model.Usuario;
import com.tienda.backend.repository.CategoriaRepository;
import com.tienda.backend.repository.ProductoRepository;
import com.tienda.backend.repository.RolRepository;
import com.tienda.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        // 1. Crear roles
        if (rolRepository.count() == 0) {
            Rol rolUser = new Rol("ROLE_USER");
            rolUser.setDescripcion("Usuario normal");

            Rol rolAdmin = new Rol("ROLE_ADMIN");
            rolAdmin.setDescripcion("Administrador");

            rolRepository.saveAll(Arrays.asList(rolUser, rolAdmin));
            System.out.println("✅ Roles creados");
        }

        // 2. Crear usuario admin
        if (usuarioRepository.count() == 0) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setEmail("admin@tienda.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setNombre("Admin");
            admin.setApellido("Principal");
            admin.setRoles(rolRepository.findAll());

            usuarioRepository.save(admin);
            System.out.println("✅ Admin creado (admin/admin123)");
        }

        // 3. Crear CATEGORÍAS CON IMÁGENES (lo nuevo)
        if (categoriaRepository.count() == 0) {

            // Categoría Laptops con imagen
            Categoria laptops = new Categoria();
            laptops.setNombre("Laptops");
            laptops.setDescripcion("Las laptops más potentes y delgadas para profesionales");
            laptops.setImagenUrl("https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=300"); // ¡Imagen real!
            laptops.setOrden(1);
            laptops.setActiva(true);

            // Categoría Smartphones con imagen
            Categoria smartphones = new Categoria();
            smartphones.setNombre("Smartphones");
            smartphones.setDescripcion("La innovación en tus manos");
            smartphones.setImagenUrl("https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=300");
            smartphones.setOrden(2);
            smartphones.setActiva(true);

            // Categoría Tablets con imagen
            Categoria tablets = new Categoria();
            tablets.setNombre("Tablets");
            tablets.setDescripcion("Versatilidad y rendimiento en un formato compacto");
            tablets.setImagenUrl("https://images.unsplash.com/photo-1544244011-0b9b3c4c7b7b?w=300");
            tablets.setOrden(3);
            tablets.setActiva(true);

            // Categoría Audio con imagen
            Categoria audio = new Categoria();
            audio.setNombre("Audio");
            audio.setDescripcion("Sumérgete en un mundo de sonido");
            audio.setImagenUrl("https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=300");
            audio.setOrden(4);
            audio.setActiva(true);

            // Guardar todas las categorías
            categoriaRepository.saveAll(Arrays.asList(laptops, smartphones, tablets, audio));
            System.out.println("✅ Categorías con imágenes creadas");
        }

        // 4. Crear productos y asignarlos a categorías
        if (productoRepository.count() == 0) {

            // Obtener las categorías guardadas
            Categoria catLaptops = categoriaRepository.findByNombre("Laptops").orElse(null);
            Categoria catSmartphones = categoriaRepository.findByNombre("Smartphones").orElse(null);
            Categoria catTablets = categoriaRepository.findByNombre("Tablets").orElse(null);
            Categoria catAudio = categoriaRepository.findByNombre("Audio").orElse(null);

            // Producto 1 - Laptop (categoría Laptops)
            Producto laptop = new Producto();
            laptop.setNombre("MacBook Pro 16");
            laptop.setDescripcion("Chip M3 Pro, 16GB RAM, 512GB SSD");
            laptop.setPrecio(2499.99);
            laptop.setCategoria(catLaptops);
            laptop.setImagenUrl("https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=300");
            laptop.setDestacado(true);

            // Producto 2 - iPhone (categoría Smartphones)
            Producto iphone = new Producto();
            iphone.setNombre("iPhone 15 Pro");
            iphone.setDescripcion("Titanio, cámara profesional, chip A17 Pro");
            iphone.setPrecio(1299.99);
            iphone.setCategoria(catSmartphones);
            iphone.setImagenUrl("https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=300");
            iphone.setDestacado(true);

            // Producto 3 - iPad (categoría Tablets)
            Producto ipad = new Producto();
            ipad.setNombre("iPad Pro 12.9");
            ipad.setDescripcion("Pantalla Liquid Retina XDR, chip M2");
            ipad.setPrecio(1099.99);
            ipad.setCategoria(catTablets);
            ipad.setImagenUrl("https://images.unsplash.com/photo-1544244011-0b9b3c4c7b7b?w=300");
            ipad.setDestacado(false);

            // Producto 4 - Audífonos (categoría Audio)
            Producto audifonos = new Producto();
            audifonos.setNombre("AirPods Max");
            audifonos.setDescripcion("Audio de alta fidelidad, cancelación de ruido");
            audifonos.setPrecio(549.99);
            audifonos.setCategoria(catAudio);
            audifonos.setImagenUrl("https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=300");
            audifonos.setDestacado(true);

            productoRepository.saveAll(Arrays.asList(laptop, iphone, ipad, audifonos));
            System.out.println("✅ Productos creados y asignados a categorías");
        }
    }
}