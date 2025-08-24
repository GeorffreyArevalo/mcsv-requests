package co.com.crediya.r2dbc.persistence.requests;

import co.com.crediya.r2dbc.entities.RequestsEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface RequestsRepository extends ReactiveCrudRepository<RequestsEntity, Long>, ReactiveQueryByExampleExecutor<RequestsEntity> {



}
