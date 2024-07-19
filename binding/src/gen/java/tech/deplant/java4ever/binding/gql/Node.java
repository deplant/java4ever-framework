package tech.deplant.java4ever.binding.gql;

import java.lang.String;
import java.util.List;
import java.util.Optional;

/**
 * This type is unstable;
 */
public sealed interface Node {
  /**
   * **UNSTABLE**
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
  record BlockchainAccount(String id, String address, Integer acc_type,
      AccountStatusEnum acc_type_name, String balance, List<OtherCurrency> balance_other,
      String bits, String boc, String cells, String code, String code_hash, String data,
      String data_hash, String due_payment, String init_code_hash, Float last_paid,
      String last_trans_lt, String library, String library_hash, String prev_code_hash,
      String proof, String public_cells, Integer split_depth, String state_hash, Boolean tick,
      Boolean tock, Integer workchain_id) implements Node {
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

  /**
   * **UNSTABLE**
       * Block;
   */
  record BlockchainBlock(String id, String hash, List<BlockAccountBlocks> account_blocks,
      Boolean after_merge, Boolean after_split, Boolean before_split, String boc,
      String chain_order, String created_by, String end_lt, String file_hash, Integer flags,
      Float gen_catchain_seqno, String gen_software_capabilities, Float gen_software_version,
      Float gen_utime, String gen_utime_string, Float gen_validator_list_hash_short,
      Integer global_id, List<InMsg> in_msg_descr, Boolean key_block, BlockMaster master,
      ExtBlkRef master_ref, Float min_ref_mc_seqno, List<OutMsg> out_msg_descr,
      ExtBlkRef prev_alt_ref, Float prev_key_block_seqno, ExtBlkRef prev_ref,
      ExtBlkRef prev_vert_alt_ref, ExtBlkRef prev_vert_ref, String rand_seed, Float seq_no,
      String shard, String start_lt, BlockStateUpdate state_update, Integer status,
      BlockProcessingStatusEnum status_name, Integer tr_count, BlockValueFlow value_flow,
      Float version, Float vert_seq_no, Boolean want_merge, Boolean want_split,
      Integer workchain_id) implements Node {
    public static QueryExecutorBuilder end_lt(String objectFieldsTree, BigIntFormat format) {
      var builder = new QueryExecutorBuilder("end_lt", objectFieldsTree);
      Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
      return builder;
    }

    public static QueryExecutorBuilder gen_software_capabilities(String objectFieldsTree,
        BigIntFormat format) {
      var builder = new QueryExecutorBuilder("gen_software_capabilities", objectFieldsTree);
      Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
      return builder;
    }

    public static QueryExecutorBuilder start_lt(String objectFieldsTree, BigIntFormat format) {
      var builder = new QueryExecutorBuilder("start_lt", objectFieldsTree);
      Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
      return builder;
    }
  }

  /**
   * **UNSTABLE**
       * # Message type
       *
       * Message layout queries.  A message consists of its header followed by its
       * body or payload. The body is essentially arbitrary, to be interpreted by the
       * destination smart contract. It can be queried with the following fields:;
   */
  record BlockchainMessage(String id, String hash, String block_id, String boc, String body,
      String body_hash, Boolean bounce, Boolean bounced, String chain_order, String code,
      String code_hash, Float created_at, String created_at_string, String created_lt, String data,
      String data_hash, String dst, Node.BlockchainTransaction dst_transaction,
      Integer dst_workchain_id, String fwd_fee, Boolean ihr_disabled, String ihr_fee,
      String import_fee, String library, String library_hash, Integer msg_type,
      MessageTypeEnum msg_type_name, String proof, Integer split_depth, String src,
      Node.BlockchainTransaction src_transaction, Integer src_workchain_id, Integer status,
      MessageProcessingStatusEnum status_name, Boolean tick, Boolean tock, String value,
      List<OtherCurrency> value_other) implements Node {
    public static QueryExecutorBuilder created_lt(String objectFieldsTree, BigIntFormat format) {
      var builder = new QueryExecutorBuilder("created_lt", objectFieldsTree);
      Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
      return builder;
    }

    public static QueryExecutorBuilder fwd_fee(String objectFieldsTree, BigIntFormat format) {
      var builder = new QueryExecutorBuilder("fwd_fee", objectFieldsTree);
      Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
      return builder;
    }

    public static QueryExecutorBuilder ihr_fee(String objectFieldsTree, BigIntFormat format) {
      var builder = new QueryExecutorBuilder("ihr_fee", objectFieldsTree);
      Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
      return builder;
    }

    public static QueryExecutorBuilder import_fee(String objectFieldsTree, BigIntFormat format) {
      var builder = new QueryExecutorBuilder("import_fee", objectFieldsTree);
      Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
      return builder;
    }

    public static QueryExecutorBuilder value(String objectFieldsTree, BigIntFormat format) {
      var builder = new QueryExecutorBuilder("value", objectFieldsTree);
      Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
      return builder;
    }
  }

  /**
   * **UNSTABLE**
       * Transaction;
   */
  record BlockchainTransaction(String id, String hash, Boolean aborted,
      Node.BlockchainAccount account, String account_addr, TransactionAction action,
      String balance_delta, List<OtherCurrency> balance_delta_other, String block_id, String boc,
      TransactionBounce bounce, String chain_order, TransactionCompute compute,
      TransactionCredit credit, Boolean credit_first, Boolean destroyed, Integer end_status,
      AccountStatusEnum end_status_name, String ext_in_msg_fee, Node.BlockchainMessage in_message,
      String in_msg, Boolean installed, String lt, String new_hash, Float now, String now_string,
      String old_hash, Integer orig_status, AccountStatusEnum orig_status_name,
      List<Node.BlockchainMessage> out_messages, List<String> out_msgs, Integer outmsg_cnt,
      String prepare_transaction, String prev_trans_hash, String prev_trans_lt, String proof,
      TransactionSplitInfo split_info, Integer status, TransactionProcessingStatusEnum status_name,
      TransactionStorage storage, String total_fees, List<OtherCurrency> total_fees_other,
      Integer tr_type, TransactionTypeEnum tr_type_name, String tt,
      Integer workchain_id) implements Node {
    public static QueryExecutorBuilder balance_delta(String objectFieldsTree, BigIntFormat format) {
      var builder = new QueryExecutorBuilder("balance_delta", objectFieldsTree);
      Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
      return builder;
    }

    public static QueryExecutorBuilder ext_in_msg_fee(String objectFieldsTree,
        BigIntFormat format) {
      var builder = new QueryExecutorBuilder("ext_in_msg_fee", objectFieldsTree);
      Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
      return builder;
    }

    public static QueryExecutorBuilder lt(String objectFieldsTree, BigIntFormat format) {
      var builder = new QueryExecutorBuilder("lt", objectFieldsTree);
      Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
      return builder;
    }

    public static QueryExecutorBuilder prev_trans_lt(String objectFieldsTree, BigIntFormat format) {
      var builder = new QueryExecutorBuilder("prev_trans_lt", objectFieldsTree);
      Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
      return builder;
    }

    public static QueryExecutorBuilder total_fees(String objectFieldsTree, BigIntFormat format) {
      var builder = new QueryExecutorBuilder("total_fees", objectFieldsTree);
      Optional.ofNullable(format).ifPresent(ar -> builder.addToQuery("format",ar));
      return builder;
    }
  }
}
