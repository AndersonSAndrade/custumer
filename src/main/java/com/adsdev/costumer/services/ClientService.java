package com.adsdev.costumer.services;

import com.adsdev.costumer.doman.dto.ClientsDTO;
import com.adsdev.costumer.doman.entity.Clients;
import com.adsdev.costumer.doman.repositories.ClientsRepository;
import com.adsdev.costumer.services.exceptions.DatabaseException;
import com.adsdev.costumer.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientsRepository clientsRepository;


    @Transactional(readOnly = true)
    public Page<ClientsDTO> findAllPaged(PageRequest pageRequest){
        Page<Clients> list = clientsRepository.findAll(pageRequest);
        return list.map(ClientsDTO::new);
    }

    @Transactional(readOnly = true)
    public ClientsDTO fidById(Long id) {
        Optional<Clients> obj = clientsRepository.findById(id);
        Clients entity = obj.orElseThrow(() -> new ResourceNotFoundException("Cliente não Encontrado!"));
        return new ClientsDTO(entity);
    }

    @Transactional(readOnly = true)
    public ClientsDTO insert(ClientsDTO dto) {
        Clients entity = new Clients();
        copyDtoToEntity(dto, entity);
        entity = clientsRepository.save(entity);
        return new ClientsDTO(entity);
    }

    @Transactional
    public ClientsDTO update(Long id, ClientsDTO dto) {
        try {
            Clients entity = clientsRepository.getOne(id);
            copyDtoToEntity(dto, entity);
            entity = clientsRepository.save(entity);
            return new ClientsDTO(entity);
        }catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Id Not Found " + id);
        }
    }

    public void delete(Long id) {
        try {
            clientsRepository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("Id Not Found " + id);
        }catch (DataIntegrityViolationException e){
            throw new DatabaseException("Violação de Integridade!");
        }
    }

    private void copyDtoToEntity(ClientsDTO dto, Clients entity) {
        entity.setName(dto.getName());
        entity.setBirthDate(dto.getBirthDate());
        entity.setChildren(dto.getChildren());
        entity.setCpf(dto.getCpf());
        entity.setIncome(dto.getIncome());
    }

}
