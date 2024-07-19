package tech.deplant.java4ever.binding.gql;

import java.lang.String;
import java.util.List;
import java.util.Optional;

public record BlockValueFlow(String created, List<OtherCurrency> created_other, String exported,
    List<OtherCurrency> exported_other, String fees_collected,
    List<OtherCurrency> fees_collected_other, String fees_imported,
    List<OtherCurrency> fees_imported_other, String from_prev_blk,
    List<OtherCurrency> from_prev_blk_other, String imported, List<OtherCurrency> imported_other,
    String minted, List<OtherCurrency> minted_other, String to_next_blk,
    List<OtherCurrency> to_next_blk_other) {
  public static QueryExecutorBuilder created(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("created", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder exported(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("exported", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder fees_collected(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("fees_collected", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder fees_imported(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("fees_imported", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder from_prev_blk(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("from_prev_blk", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder imported(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("imported", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder minted(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("minted", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder to_next_blk(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("to_next_blk", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }
}
