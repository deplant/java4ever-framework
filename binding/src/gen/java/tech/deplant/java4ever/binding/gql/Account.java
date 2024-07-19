package tech.deplant.java4ever.binding.gql;

import java.lang.String;
import java.util.List;
import java.util.Optional;

/**
 * # Account type
     *
     * Recall that a smart contract and an account are the same thing in the context
     * of the TON Blockchain, and that these terms can be used interchangeably, at
     * least as long as only small (or “usual”) smart contracts are considered. A large
     * smart-contract may employ several accounts lying in different shardchains of
     * the same workchain for load balancing purposes.
     *
     * An account is identified by its full address and is completely described by
     * its state. In other words, there is nothing else in an account apart from its
     * address and state.;
 */
public record Account(String id, Integer acc_type, AccountStatusEnum acc_type_name, String balance,
    List<OtherCurrency> balance_other, String bits, String boc, String cells, String code,
    String code_hash, String data, String data_hash, String due_payment, String init_code_hash,
    Float last_paid, String last_trans_lt, String library, String library_hash,
    String prev_code_hash, String proof, String public_cells, Integer split_depth,
    String state_hash, Boolean tick, Boolean tock, Integer workchain_id) {
  public static QueryExecutorBuilder balance(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("balance", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder bits(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("bits", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder cells(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("cells", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder due_payment(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("due_payment", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder last_trans_lt(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("last_trans_lt", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }

  public static QueryExecutorBuilder public_cells(String objectFieldsTree, BigIntFormat format) {
    var builder = new QueryExecutorBuilder("public_cells", objectFieldsTree);
    Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
    return builder;
  }
}
