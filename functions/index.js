

const {onUserCreated} = require("firebase-functions/v2/identity");
const admin = require("firebase-admin");

admin.initializeApp();

exports.setSupabaseRole = onUserCreated(async (event) => {
  const user = event.data;

  if (!user) {
    return;
  }

  await admin.auth().setCustomUserClaims(user.uid, {
    role: "authenticated",
  });

  console.log(`Set authenticated role for user: ${user.uid}`);
});
