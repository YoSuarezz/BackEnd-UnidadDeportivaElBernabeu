package co.edu.uco.unidaddeportivaelbernabeu.business.fachade.concrete;


import co.edu.uco.unidaddeportivaelbernabeu.business.assembler.dto.concrete.DeporteDTODomainAssembler;
import co.edu.uco.unidaddeportivaelbernabeu.business.fachade.ConsultarDeportesFachada;
import co.edu.uco.unidaddeportivaelbernabeu.business.usecase.ConsultarDeportes;
import co.edu.uco.unidaddeportivaelbernabeu.business.usecase.concrete.ConsultarDeportesImpl;
import co.edu.uco.unidaddeportivaelbernabeu.crosscutting.exceptions.UnidadDeportivaElBernabeuException;
import co.edu.uco.unidaddeportivaelbernabeu.crosscutting.exceptions.custom.BusinessUDElBernabeuException;
import co.edu.uco.unidaddeportivaelbernabeu.data.dao.factory.DAOFactory;
import co.edu.uco.unidaddeportivaelbernabeu.data.dao.factory.enums.Factory;
import co.edu.uco.unidaddeportivaelbernabeu.dto.DeporteDTO;

import java.util.List;

public final class ConsultarDeportesFachadaImpl implements ConsultarDeportesFachada {

    private DAOFactory factory;

    public ConsultarDeportesFachadaImpl(){
        factory = DAOFactory.getFactory(Factory.POSTGRESQL);
    }
    @Override
    public final List<DeporteDTO> execute(final DeporteDTO deporte) {
        try {
            var deporteDomain = DeporteDTODomainAssembler.obtenerInstancia().ensamblarDominio(deporte);

            final ConsultarDeportes useCase = new ConsultarDeportesImpl(factory);
            var resultados = useCase.ejecutar(deporteDomain);
            return DeporteDTODomainAssembler.obtenerInstancia()
                    .ensamblarListaDTO(resultados);

        }catch (UnidadDeportivaElBernabeuException exception){
            throw exception;
        }catch (Exception exception){
            var mensajeUsuario = "Se ha presentado un problema tratando de consultar la informacion de los paises";
            var mensajeTecnico = "Se ha presentado un problema INESPERADO tratando de consultar la informacion de los paises";

            throw new BusinessUDElBernabeuException(mensajeTecnico, mensajeUsuario);
        } finally {
            factory.cerrarConexion();
        }
    }


}