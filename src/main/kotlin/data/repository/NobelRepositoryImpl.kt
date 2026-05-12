package org.example.data.repository

import org.example.domain.model.Laureate
import org.example.domain.model.NobelPrize
import org.example.domain.repository.NobelRepository

class NobelRepositoryImpl : NobelRepository {
    private val prizes: List<NobelPrize> = listOf(
        NobelPrize(
            year = 2026, category = "physics",
            overallMotivation = "for experimental methods that generate attosecond pulses of light",
            laureates = listOf(
                Laureate(1, "Pierre",  "Agostini", "for experimental methods that generate attosecond pulses of light for the study of electron dynamics in matter", 3),
                Laureate(2, "Ferenc",  "Krausz",   "for experimental methods that generate attosecond pulses of light for the study of electron dynamics in matter", 3),
                Laureate(3, "Anne",    "L'Huillier","for experimental methods that generate attosecond pulses of light for the study of electron dynamics in matter", 3)
            )
        ),
        NobelPrize(
            year = 2026, category = "chemistry",
            overallMotivation = "for the discovery and synthesis of quantum dots",
            laureates = listOf(
                Laureate(4, "Moungi",  "Bawendi",  "for the discovery and synthesis of quantum dots", 3),
                Laureate(5, "Louis",   "Brus",     "for the discovery and synthesis of quantum dots", 3),
                Laureate(6, "Alexei",  "Ekimov",   "for the discovery and synthesis of quantum dots", 3)
            )
        ),
        NobelPrize(
            year = 2026, category = "medicine",
            overallMotivation = null,
            laureates = listOf(
                Laureate(7,  "Katalin", "Karikó",   "for their discoveries concerning nucleoside base modifications that enabled the development of effective mRNA vaccines", 2),
                Laureate(8,  "Drew",    "Weissman", "for their discoveries concerning nucleoside base modifications that enabled the development of effective mRNA vaccines", 2)
            )
        ),
        NobelPrize(
            year = 2026, category = "literature",
            overallMotivation = null,
            laureates = listOf(
                Laureate(9, "Jon", "Fosse", "who gives voice to the unsayable", 1)
            )
        ),
        NobelPrize(
            year = 2026, category = "peace",
            overallMotivation = null,
            laureates = listOf(
                Laureate(10, "Narges", "Mohammadi", "for her fight against the oppression of women in Iran and her efforts to promote human rights", 1)
            )
        ),
        NobelPrize(
            year = 2026, category = "economics",
            overallMotivation = null,
            laureates = listOf(
                Laureate(11, "Claudia", "Goldin", "for having advanced our understanding of women's labour market outcomes", 1)
            )
        ),
        NobelPrize(
            year = 2025, category = "physics",
            overallMotivation = "for experiments with entangled photons",
            laureates = listOf(
                Laureate(12, "Alain",  "Aspect",      "for experiments with entangled photons, establishing the violation of Bell inequalities", 3),
                Laureate(13, "John",   "Clauser",     "for experiments with entangled photons, establishing the violation of Bell inequalities", 3),
                Laureate(14, "Anton",  "Zeilinger",   "for experiments with entangled photons, establishing the violation of Bell inequalities", 3)
            )
        ),
        NobelPrize(
            year = 2025, category = "chemistry",
            overallMotivation = null,
            laureates = listOf(
                Laureate(15, "Carolyn", "Bertozzi",  "for the development of click chemistry and bioorthogonal chemistry", 3),
                Laureate(16, "Morten",  "Meldal",    "for the development of click chemistry and bioorthogonal chemistry", 3),
                Laureate(17, "K. Barry","Sharpless", "for the development of click chemistry and bioorthogonal chemistry", 3)
            )
        ),
        NobelPrize(
            year = 2025, category = "medicine",
            overallMotivation = null,
            laureates = listOf(
                Laureate(18, "Svante", "Pääbo", "for his discoveries concerning the genomes of extinct hominins and human evolution", 1)
            )
        ),
        NobelPrize(
            year = 2025, category = "literature",
            overallMotivation = null,
            laureates = listOf(
                Laureate(19, "Annie", "Ernaux", "for the courage and clinical acuity with which she uncovers the roots, estrangements and collective restraints of personal memory", 1)
            )
        ),
        NobelPrize(
            year = 2025, category = "peace",
            overallMotivation = null,
            laureates = listOf(
                Laureate(20, "Ales",  "Bialiatski",              "for their efforts to promote human rights, democracy and peaceful coexistence in Russia, Belarus and Ukraine", 3),
                Laureate(21, "Memorial", null,                   "for their efforts to promote human rights, democracy and peaceful coexistence in Russia, Belarus and Ukraine", 3),
                Laureate(22, "Center for Civil Liberties", null, "for their efforts to promote human rights, democracy and peaceful coexistence in Russia, Belarus and Ukraine", 3)
            )
        ),
        NobelPrize(
            year = 2025, category = "economics",
            overallMotivation = null,
            laureates = listOf(
                Laureate(23, "Ben",    "Bernanke", "for research on banks and financial crises", 3),
                Laureate(24, "Douglas","Diamond",  "for research on banks and financial crises", 3),
                Laureate(25, "Philip", "Dybvig",   "for research on banks and financial crises", 3)
            )
        )
    )

    override fun getAllPrizes(): List<NobelPrize> = prizes

    override fun getPrize(year: Int, category: String): NobelPrize? =
        prizes.firstOrNull { it.year == year && it.category.equals(category, ignoreCase = true) }

    override fun getLaureates(year: Int, category: String): List<Laureate> =
        getPrize(year, category)?.laureates ?: emptyList()
}