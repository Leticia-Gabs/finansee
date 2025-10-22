//import org.springframework.data.jpa.domain.Specification;
//import java.time.LocalDate;
//import java.math.BigDecimal;
//
//public class DespesaSpecifications {
//
//    public static Specification<Despesa> pertenceAoUsuario(Long usuarioId) {
//        return (root, query, cb) -> cb.equal(root.get("usuario").get("id"), usuarioId);
//    }
//
//    public static Specification<Despesa> comCategoria(String categoria) {
//        return (root, query, cb) -> cb.equal(root.get("categoria").get("nome"), categoria);
//    }
//
//    public static Specification<Despesa> dataMaiorOuIgual(LocalDate dataInicio) {
//        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("data"), dataInicio);
//    }
//
//    public static Specification<Despesa> dataMenorOuIgual(LocalDate dataFim) {
//        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("data"), dataFim);
//    }
//
//    public static Specification<Despesa> valorMaiorOuIgual(BigDecimal valorMin) {
//        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("valor"), valorMin);
//    }
//
//    public static Specification<Despesa> valorMenorOuIgual(BigDecimal valorMax) {
//        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("valor"), valorMax);
//    }
//}
