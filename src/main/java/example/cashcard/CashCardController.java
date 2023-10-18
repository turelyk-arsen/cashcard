package example.cashcard;

import java.net.URI;
import java.security.Principal;
import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/cashcards")
public class CashCardController {
	private CashCardRepository cashCardRepository;
	
	public CashCardController(CashCardRepository cashCardRepository) {
		this.cashCardRepository = cashCardRepository;
	}
	
	@GetMapping("/{requestId}")
	public ResponseEntity<CashCard> findById(@PathVariable Long requestId, Principal principal) {
		Optional<CashCard> cashCardOptional = Optional.ofNullable(cashCardRepository.findByIdAndOwner(requestId, principal.getName()));
//				cashCardRepository.findById(requestId);
//		if(requestId.equals(99L)) {
//		CashCard cashCard = new CashCard(99L, 123.45);
//		return ResponseEntity.ok(cashCard);
		if(cashCardOptional.isPresent()) {
			return ResponseEntity.ok(cashCardOptional.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping
	private ResponseEntity<Void> createCashCard(@RequestBody CashCard newCashCardRequest, UriComponentsBuilder ucb, Principal principal) {
		CashCard cashCardWithOwner = new CashCard(null, newCashCardRequest.amount(), principal.getName());
		CashCard savedCashCard = cashCardRepository.save(cashCardWithOwner);
		URI locationOfNewCashCard = ucb.path("cashcards/{id}")
				.buildAndExpand(savedCashCard.id())
				.toUri();
		
		return ResponseEntity.created(locationOfNewCashCard).build();
	}
	
	@GetMapping
	public ResponseEntity<List<CashCard>> findAll(Pageable pageable, Principal principal) {
		Page<CashCard> page = cashCardRepository.findByOwner(principal.getName(), PageRequest
				.of(pageable.getPageNumber(),
						pageable.getPageSize(),
						pageable.getSortOr(Sort.by(Sort.Direction.ASC, "amount"))));
				
		return ResponseEntity.ok(page.getContent());
	}

}
