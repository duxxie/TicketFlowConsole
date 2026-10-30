package service;

import pattern.decorator.IIngresso;

/** Validação de ingressos na portaria do evento. */
public class CheckInService {
    private final CompraService compraService;

    public CheckInService(CompraService compraService) {
        this.compraService = compraService;
    }

    public IIngresso fazerCheckIn(String codigo) {
        IIngresso ingresso = compraService.buscarIngresso(codigo);
        ingresso.fazerCheckIn();
        return ingresso;
    }
}
