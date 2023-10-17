package example.cashcard;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cashcards")
public class CashCardController {
	private CashCardRepository cashCardRepository;
	
	public CashCardController(CashCardRepository cashCardRepository) {
		this.cashCardRepository = cashCardRepository;
	}
	
	@GetMapping("/{requestId}")
	public ResponseEntity<CashCard> findById(@PathVariable Long requestId) {
		Optional<CashCard> cashCardOptional = cashCardRepository.findById(requestId);
//		if(requestId.equals(99L)) {
//		CashCard cashCard = new CashCard(99L, 123.45);
//		return ResponseEntity.ok(cashCard);
		if(cashCardOptional.isPresent()) {
			return ResponseEntity.ok(cashCardOptional.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

}
