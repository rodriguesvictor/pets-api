package one.digital.innovation.petapi.service;

import lombok.AllArgsConstructor;
import one.digital.innovation.petapi.dto.request.PetDTO;
import one.digital.innovation.petapi.dto.response.MessageResponseDTO;
import one.digital.innovation.petapi.entity.Pet;
import one.digital.innovation.petapi.exception.PetNotFoundException;
import one.digital.innovation.petapi.mapper.PetMapper;
import one.digital.innovation.petapi.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PetService {

    private PetRepository petRepository;

    private final PetMapper petMapper = PetMapper.INSTANCE;

    public MessageResponseDTO createPet(PetDTO petDTO){

        Pet petToSave = petMapper.toModel(petDTO);

        Pet savedPet = petRepository.save(petToSave);
        return createMessageResponse(savedPet.getId(), "Created Pet with ID ");
    }


    public List<PetDTO> listAll() {
        List<Pet> allPets = petRepository.findAll();
        return allPets.stream()
                .map(petMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PetDTO findById(Long id) throws PetNotFoundException {
        Pet pet = verifyIfExists(id);

        return petMapper.toDTO(pet);
    }

    public void deletePet(Long id) throws PetNotFoundException {
        verifyIfExists(id);
        petRepository.deleteById(id);
    }

    public MessageResponseDTO updateById(Long id, PetDTO petDTO) throws PetNotFoundException {
        verifyIfExists(id);
        Pet petToUpdate = petMapper.toModel(petDTO);

        Pet updatedPet = petRepository.save(petToUpdate);
        return createMessageResponse(updatedPet.getId(), "Updated Pet with ID ");
    }

    private Pet verifyIfExists (Long id) throws PetNotFoundException {
        return petRepository.findById(id)
                .orElseThrow(() -> new PetNotFoundException(id));
    }
    
    private MessageResponseDTO createMessageResponse(Long id, String message) {
        return MessageResponseDTO
                .builder()
                .message(message + id)
                .build();
    }
}
