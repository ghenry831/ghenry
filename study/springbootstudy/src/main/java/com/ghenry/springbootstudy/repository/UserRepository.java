package com.ghenry.springbootstudy.repository;

import com.ghenry.springbootstudy.pojo.User;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class UserRepository {

    private final ConcurrentHashMap<Integer,User> repository = new ConcurrentHashMap<>();

    private final static AtomicInteger idGenerator = new AtomicInteger();

    public boolean save(User user){

        Integer id = idGenerator.incrementAndGet();

        user.setId(id);

        return repository.put(id,user)==null;
    }

    public Collection<User> findAll(){
        return repository.values();
    }
}
