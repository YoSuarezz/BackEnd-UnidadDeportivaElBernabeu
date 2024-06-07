package co.edu.uco.unidaddeportivaelbernabeu.business.fachade.concrete;

import co.edu.uco.unidaddeportivaelbernabeu.business.assembler.dto.concrete.TipoEspacioDeportivoDTODomainAssembler;
import co.edu.uco.unidaddeportivaelbernabeu.business.fachade.FacadeWithoutReturn;
import co.edu.uco.unidaddeportivaelbernabeu.business.usecase.concrete.ActualizarTipoEspacioDeportivoImpl;
import co.edu.uco.unidaddeportivaelbernabeu.crosscutting.exceptions.UnidadDeportivaElBernabeuException;
import co.edu.uco.unidaddeportivaelbernabeu.crosscutting.exceptions.custom.BusinessUDElBernabeuException;
import co.edu.uco.unidaddeportivaelbernabeu.crosscutting.exceptions.messagecatalog.MessageCatalogStrategy;
import co.edu.uco.unidaddeportivaelbernabeu.crosscutting.exceptions.messagecatalog.data.CodigoMensaje;
import co.edu.uco.unidaddeportivaelbernabeu.data.dao.factory.DAOFactory;
import co.edu.uco.unidaddeportivaelbernabeu.data.dao.factory.enums.Factory;
import co.edu.uco.unidaddeportivaelbernabeu.dto.TipoEspacioDeportivoDTO;

public class ActualizarTipoEspacioDeportivoFachadaImpl implements FacadeWithoutReturn<TipoEspacioDeportivoDTO> {

    private final DAOFactory factory;

    public ActualizarTipoEspacioDeportivoFachadaImpl() {
        this.factory = DAOFactory.getFactory(Factory.AZURE_SQL);
    }

    @Override
    public void ejecutar(TipoEspacioDeportivoDTO tipoEspacioDeportivo) {
        factory.iniciarTransaccion();

        try {
            var usecase = new ActualizarTipoEspacioDeportivoImpl(factory);
            var tipoEspacioDeportivoDomain = TipoEspacioDeportivoDTODomainAssembler.obtenerInstancia().ensamblarDominio(tipoEspacioDeportivo);
            usecase.ejecutar(tipoEspacioDeportivoDomain);

            factory.confirmarTransaccion();
        } catch (final UnidadDeportivaElBernabeuException exception) {
            factory.cancelarTransaccion();
            throw exception;
        } catch (final Exception exception) {
            factory.cancelarTransaccion();

            var mensajeUsuario = MessageCatalogStrategy.getContenidoMensaje(CodigoMensaje.M00091);
            var mensajeTecnico = MessageCatalogStrategy.getContenidoMensaje(CodigoMensaje.M00092);

            throw new BusinessUDElBernabeuException(mensajeUsuario, mensajeTecnico, exception);
        } finally {
            factory.cerrarConexion();
        }
    }
}
