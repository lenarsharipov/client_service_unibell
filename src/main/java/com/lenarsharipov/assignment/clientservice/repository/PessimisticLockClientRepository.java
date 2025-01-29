package com.lenarsharipov.assignment.clientservice.repository;

import com.lenarsharipov.assignment.clientservice.model.Client;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Repository
@Slf4j
public class PessimisticLockClientRepository {

    private final Map<Long, Client> clientStorage = new ConcurrentHashMap<>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private final AtomicLong clientIdSeq = new AtomicLong(0);

    private final ThreadLocal<Map<Long, Client>> transactionCache =
            ThreadLocal.withInitial(HashMap::new);

    public void beginTransaction() {
        log.debug("beginTransaction");
        lock.writeLock().lock();
        log.debug("write lock acquired");
        transactionCache.set(new HashMap<>());
    }

    public void save(Client client) {
        lock.writeLock().lock();
        log.debug("write lock acquired");
        try {
            if (client.getId() == null) {
                client.setId(clientIdSeq.incrementAndGet());
            }
            transactionCache.get().put(client.getId(), client);
            log.debug("save");
        } finally {
            lock.writeLock().unlock();
            log.debug("write lock released");
        }
    }

    public void commitTransaction() {
        try {
            log.debug("commitTransaction");
            clientStorage.putAll(transactionCache.get());
        } finally {
            transactionCache.remove();
            lock.writeLock().unlock();
            log.debug("write lock released");
        }
    }

    public void rollbackTransaction() {
        try {
            log.debug("rollbackTransaction");
            transactionCache.remove();
        } finally {
            lock.writeLock().unlock();
            log.debug("write lock released");
        }
    }

    public Optional<Client> findById(Long id) {
        lock.readLock().lock();
        log.debug("read lock acquired");
        try {
            log.debug("findById");
            return Optional.ofNullable(clientStorage.get(id));
        } finally {
            lock.readLock().unlock();
            log.debug("read lock released");
        }
    }

    public List<Client> findAll() {
        lock.readLock().lock();
        log.debug("read lock acquired");
        try {
            log.debug("findAll");
            return new ArrayList<>(clientStorage.values());
        } finally {
            lock.readLock().unlock();
            log.debug("read lock released");
        }
    }

    public void deleteById(Long id) {
        lock.writeLock().lock();
        log.debug("write lock acquired");

        try {
            log.debug("deleteById");
            clientStorage.remove(id);
        } finally {
            lock.writeLock().unlock();
            log.debug("write lock released");
        }
    }
}
