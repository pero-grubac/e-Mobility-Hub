package org.unibl.etf.emobility_hub_promotions.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HtmlTest {

    @Test
    void escapesMarkupSoUserTextCannotInjectScripts() {
        assertThat(Html.escape("<script>alert('x')</script> & \"q\""))
                .isEqualTo("&lt;script&gt;alert(&#39;x&#39;)&lt;/script&gt; &amp; &quot;q&quot;");
    }

    @Test
    void nullBecomesAnEmptyString() {
        assertThat(Html.escape(null)).isEmpty();
        assertThat(Html.preview(null, 5)).isEmpty();
    }

    @Test
    void previewShortensLongTextAndEscapesIt() {
        assertThat(Html.preview("Rent <any> bicycle", 10)).isEqualTo("Rent &lt;any&gt;...");
        assertThat(Html.preview("Short", 10)).isEqualTo("Short");
    }
}
