package co.edu.uco.unidaddeportivaelbernabeu.business.usecase.concrete.espaciosdeportivos;

import co.edu.uco.unidaddeportivaelbernabeu.business.assembler.entity.concrete.espaciosdeportivos.DeporteEntityDomainAssembler;
import co.edu.uco.unidaddeportivaelbernabeu.business.domain.espaciosdeportivos.DeporteDomain;
import co.edu.uco.unidaddeportivaelbernabeu.business.usecase.UseCaseWithReturn;
import co.edu.uco.unidaddeportivaelbernabeu.data.dao.factory.DAOFactory;

import java.util.List;

public final class ConsultarDeportesImpl implements UseCaseWithReturn<DeporteDomain, List<DeporteDomain>> {

    private final DAOFactory factory;

    public ConsultarDeportesImpl(final DAOFactory factory) {
        this.factory = factory;
    }

    @Override
    public final List<DeporteDomain> ejecutar(final DeporteDomain deporte) {
        var deporteEntity = DeporteEntityDomainAssembler.obtenerInstancia().ensamblarEntidad(deporte);
        var resultados = factory.getDeporteDAO().consultar(deporteEntity);

        return DeporteEntityDomainAssembler.obtenerInstancia().ensamblarListaDominios(resultados);
    }
}
