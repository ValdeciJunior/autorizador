package com.vr.miniautorizador.api.mapper;

import com.vr.miniautorizador.api.dto.CartaoRequest;
import com.vr.miniautorizador.domain.model.Cartao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CartaoMapper {

    //Pra chamar o mapeador diretamente através dessa constante de qualquer lugar do projeto
    CartaoMapper INSTANCE = Mappers.getMapper(CartaoMapper.class);

    //O id é gerado automaticamente pelo banco de dados (IDENTITY).
    @Mapping(target = "id", ignore = true)
    //O saldo inicial do cartão é definido direto no construtor da entidade (new BigDecimal("500.00")),
    // e não enviado pelo usuário no payload.
    @Mapping(target = "saldo", ignore = true)
        // Mapeia do DTO para a nossa Entidade de banco
    Cartao toEntity(CartaoRequest request);

    // Mapeia da Entidade de volta para o DTO (usado no retorno do POST /cartoes)
    CartaoRequest toDto(Cartao cartao);
}