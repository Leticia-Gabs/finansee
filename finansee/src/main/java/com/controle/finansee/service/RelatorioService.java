package com.controle.finansee.service;

import com.controle.finansee.dto.GastoPorCategoriaDTO;
import com.controle.finansee.dto.TransacaoDTO;
import com.controle.finansee.model.user.User;
import com.controle.finansee.repository.DespesaRepository;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
public class RelatorioService {
    @Autowired
    private DespesaRepository despesaRepository;

    @Autowired
    private TransacaoService transacaoService;

    public List<GastoPorCategoriaDTO> getGastosPorCategoria(User usuario, int ano, int mes) {
        return despesaRepository.findGastosPorCategoria(usuario.getId(), ano, mes);
    }

    public ByteArrayInputStream gerarPdfTransacoes(User usuario, int ano, int mes) throws IOException {

        YearMonth anoMes     = YearMonth.of(ano, mes);
        LocalDate dataInicio = anoMes.atDay(1);
        LocalDate dataFim    = anoMes.atEndOfMonth();

        // busca os dados filtrados
        List<TransacaoDTO> transacoes = transacaoService.listarTransacoes(
                usuario, null, null, dataInicio, dataFim, null, null
        );

        // configurando Itext para gerar PDF em memória
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        // 4. Adiciona Título
        Paragraph titulo = new Paragraph(String.format("Relatório de Transações - %02d/%d", mes, ano))
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(20)
                .setBold();
        document.add(titulo);
        document.add(new Paragraph("\n"));

        // 5. Cria a Tabela
        float[] columnWidths = {2, 4, 3, 2, 2};
        Table table = new Table(UnitValue.createPercentArray(columnWidths));
        table.setWidth(UnitValue.createPercentValue(100));

        // 6. Adiciona Cabeçalhos
        table.addHeaderCell(new Cell().add(new Paragraph("Data")).setBackgroundColor(ColorConstants.GRAY).setFontColor(ColorConstants.WHITE));
        table.addHeaderCell(new Cell().add(new Paragraph("Descrição")).setBackgroundColor(ColorConstants.GRAY).setFontColor(ColorConstants.WHITE));
        table.addHeaderCell(new Cell().add(new Paragraph("Categoria")).setBackgroundColor(ColorConstants.GRAY).setFontColor(ColorConstants.WHITE));
        table.addHeaderCell(new Cell().add(new Paragraph("Conta")).setBackgroundColor(ColorConstants.GRAY).setFontColor(ColorConstants.WHITE));
        table.addHeaderCell(new Cell().add(new Paragraph("Valor (R$)")).setBackgroundColor(ColorConstants.GRAY).setFontColor(ColorConstants.WHITE));

        // 7. Adiciona os Dados (Transações)
        for (TransacaoDTO t : transacoes) {
            table.addCell(t.data().toString());
            table.addCell(t.descricao());
            table.addCell(t.categoriaNome());
            table.addCell(t.conta() != null ? t.conta() : "");

            // Adiciona cor para despesa/receita
            Cell valorCell = new Cell().add(new Paragraph(String.format("%.2f", t.valor())));
            if (t.valor().doubleValue() < 0) {
                valorCell.setFontColor(ColorConstants.RED);
            } else {
                valorCell.setFontColor(ColorConstants.GREEN);
            }
            table.addCell(valorCell);
        }

        // 8. Finaliza o documento
        document.add(table);
        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }
}
