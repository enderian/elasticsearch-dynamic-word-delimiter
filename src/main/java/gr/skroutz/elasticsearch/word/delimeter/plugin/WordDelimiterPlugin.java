package gr.skroutz.elasticsearch.word.delimeter.plugin;

import gr.skroutz.elasticsearch.word.delimeter.module.WordDelimiterRunnable;
import gr.skroutz.elasticsearch.word.delimeter.module.WordDelimiterService;
import org.elasticsearch.common.settings.Setting;
import org.elasticsearch.index.analysis.TokenFilterFactory;
import org.elasticsearch.indices.analysis.AnalysisModule;
import org.elasticsearch.plugins.AnalysisPlugin;
import org.elasticsearch.common.component.LifecycleComponent;
import org.elasticsearch.plugins.Plugin;

import java.util.*;
import java.util.function.Function;

import gr.skroutz.elasticsearch.word.delimeter.analysis.WordDelimiterTokenFilterFactory;

public class WordDelimiterPlugin extends Plugin implements AnalysisPlugin {

  private final Collection<Class<? extends LifecycleComponent>> services = new ArrayList<>();

  public WordDelimiterPlugin() {
    services.add(WordDelimiterService.class);
  }

  @Override
  public Collection<Class<? extends LifecycleComponent>> getGuiceServiceClasses() {
    return Collections.singleton(WordDelimiterService.class);
  }

  @Override
  public Map<String, AnalysisModule.AnalysisProvider<TokenFilterFactory>> getTokenFilters() {
    return Collections.singletonMap("dynamic_word_delimiter",
            WordDelimiterTokenFilterFactory::new);
  }

  @Override
  public List<Setting<?>> getSettings() {
    return Arrays.asList(
      new Setting<>(
        "plugin.dynamic_word_delimiter.protected_words_index",
        WordDelimiterRunnable.INDEX_NAME,
        Function.identity(),
        Setting.Property.NodeScope),
      Setting.timeSetting(
        "plugin.dynamic_word_delimiter.refresh_interval",
        WordDelimiterRunnable.REFRESH_INTERVAL,
        Setting.Property.NodeScope)
    );
  }

}
