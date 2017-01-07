package eg.boot.clioptions

case class CliOptions(
    twitterOAuthConsumerKey: String,
    twitterOAuthConsumerSecret: String,
    twitterOAuthToken: String,
    twitterOAuthTokenSecret: String
) {
  def isEmpty = {
    twitterOAuthConsumerKey.isEmpty() &&
    twitterOAuthConsumerSecret.isEmpty() &&
    twitterOAuthToken.isEmpty() &&
    twitterOAuthTokenSecret.isEmpty()
  }

}

object CliOptions {
  def empty = CliOptions("", "", "", "")
}
