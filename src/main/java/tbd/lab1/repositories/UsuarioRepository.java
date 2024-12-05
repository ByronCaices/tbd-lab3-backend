package tbd.lab1.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import tbd.lab1.entities.UsuarioEntity;

import java.util.List;
import java.util.Optional;

@Repository
public class UsuarioRepository extends MongoRepository<UsuarioEntity,String> implements UsuarioRepositoryInt {




}
