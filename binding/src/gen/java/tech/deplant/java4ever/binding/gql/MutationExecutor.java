package tech.deplant.java4ever.binding.gql;

import java.lang.String;
import java.util.List;
import java.util.Optional;

class MutationExecutor {
  public static QueryExecutorBuilder postRequests(String objectFieldsTree, List<Request> requests,
      String accessKey) {
    var builder = new QueryExecutorBuilder("postRequests", objectFieldsTree);
    Optional.ofNullable(requests).ifPresent(ar -> builder.addToQuery("requests",ar));
    Optional.ofNullable(accessKey).ifPresent(ar -> builder.addToQuery("accessKey",ar));
    return builder;
  }

  public static QueryExecutorBuilder registerAccessKeys(String objectFieldsTree, String account,
      List<AccessKey> keys, String signedManagementAccessKey) {
    var builder = new QueryExecutorBuilder("registerAccessKeys", objectFieldsTree);
    Optional.ofNullable(account).ifPresent(ar -> builder.addToQuery("account",ar));
    Optional.ofNullable(keys).ifPresent(ar -> builder.addToQuery("keys",ar));
    Optional.ofNullable(signedManagementAccessKey).ifPresent(ar -> builder.addToQuery("signedManagementAccessKey",ar));
    return builder;
  }

  public static QueryExecutorBuilder revokeAccessKeys(String objectFieldsTree, String account,
      List<String> keys, String signedManagementAccessKey) {
    var builder = new QueryExecutorBuilder("revokeAccessKeys", objectFieldsTree);
    Optional.ofNullable(account).ifPresent(ar -> builder.addToQuery("account",ar));
    Optional.ofNullable(keys).ifPresent(ar -> builder.addToQuery("keys",ar));
    Optional.ofNullable(signedManagementAccessKey).ifPresent(ar -> builder.addToQuery("signedManagementAccessKey",ar));
    return builder;
  }

  public static QueryExecutorBuilder finishOperations(String objectFieldsTree,
      List<String> operationIds) {
    var builder = new QueryExecutorBuilder("finishOperations", objectFieldsTree);
    Optional.ofNullable(operationIds).ifPresent(ar -> builder.addToQuery("operationIds",ar));
    return builder;
  }
}
