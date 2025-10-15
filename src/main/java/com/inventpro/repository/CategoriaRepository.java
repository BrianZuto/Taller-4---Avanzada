package com.inventpro.repository;

import com.inventpro.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    
    List<Categoria> findByActivoTrue();
    
    Optional<Categoria> findByNombreAndActivoTrue(String nombre);
    
    boolean existsByNombreAndActivoTrue(String nombre);
    
    @Query("SELECT c FROM Categoria c WHERE c.activo = true AND " +
           "(LOWER(c.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
           "LOWER(c.descripcion) LIKE LOWER(CONCAT('%', :busqueda, '%')))")
    List<Categoria> buscarPorNombreODescripcion(String busqueda);
}
